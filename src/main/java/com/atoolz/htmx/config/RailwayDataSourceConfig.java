package com.atoolz.htmx.config;

import com.zaxxer.hikari.HikariDataSource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RailwayDataSourceConfig {

  private static final Logger log = LoggerFactory.getLogger(RailwayDataSourceConfig.class);

  @Bean
  @Primary
  public DataSource dataSource() {
    String raw = System.getenv("DATABASE_URL");

    // Local development fallback: use individual env vars or hardcoded defaults
    if (raw == null || raw.isBlank()) {
      log.warn("DATABASE_URL not set — falling back to local PostgreSQL defaults");
      String host = getEnvOrDefault("DB_HOST", "localhost");
      String port = getEnvOrDefault("DB_PORT", "5432");
      String database = getEnvOrDefault("DB_NAME", "todos");
      String user = getEnvOrDefault("DB_USER", "postgres");
      String password = getEnvOrDefault("DB_PASSWORD", "postgres");
      String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;
      return buildDataSource(jdbcUrl, user, password, false);
    }

    // Railway / production: parse postgres:// or postgresql:// URI
    String url = raw.trim();
    if (url.startsWith("postgres://")) {
      url = "postgresql://" + url.substring("postgres://".length());
    }
    if (!url.startsWith("postgresql://")) {
      throw new IllegalArgumentException(
          "DATABASE_URL must start with postgres:// or postgresql://, got: " + url.substring(0, Math.min(20, url.length())));
    }

    String rest = url.substring("postgresql://".length());
    int at = rest.lastIndexOf('@');
    if (at < 0) {
      throw new IllegalArgumentException("DATABASE_URL is missing credentials (expected user:pass@host/db)");
    }

    String userInfo = rest.substring(0, at);
    String hostPart = rest.substring(at + 1);

    int colon = userInfo.indexOf(':');
    if (colon < 0) {
      throw new IllegalArgumentException("DATABASE_URL is missing password (expected user:pass@host/db)");
    }
    String user = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
    String password = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);

    int slash = hostPart.indexOf('/');
    if (slash < 0) {
      throw new IllegalArgumentException("DATABASE_URL is missing database name");
    }
    String hostPort = hostPart.substring(0, slash);
    String dbAndQuery = hostPart.substring(slash + 1);

    String host;
    String port;
    int hpColon = hostPort.lastIndexOf(':');
    if (hpColon > 0 && hostPort.indexOf(']') < 0) {
      host = hostPort.substring(0, hpColon);
      port = hostPort.substring(hpColon + 1);
    } else {
      host = hostPort;
      port = "5432";
    }

    int q = dbAndQuery.indexOf('?');
    String database = q >= 0 ? dbAndQuery.substring(0, q) : dbAndQuery;
    String existingQuery = q >= 0 ? dbAndQuery.substring(q + 1) : "";

    boolean isRemote = !host.equals("localhost") && !host.equals("127.0.0.1");
    StringBuilder jdbc = new StringBuilder();
    jdbc.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(database);
    if (!existingQuery.isEmpty()) {
      jdbc.append("?").append(existingQuery);
    } else if (isRemote) {
      jdbc.append("?sslmode=require");
    }

    log.info("Connecting to PostgreSQL at {}:{}/{}", host, port, database);
    return buildDataSource(jdbc.toString(), user, password, isRemote);
  }

  private DataSource buildDataSource(String jdbcUrl, String user, String password, boolean isRemote) {
    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(jdbcUrl);
    ds.setUsername(user);
    ds.setPassword(password);
    // Conservative pool size for Railway free tier (max 5 connections)
    ds.setMaximumPoolSize(isRemote ? 3 : 5);
    ds.setMinimumIdle(1);
    ds.setConnectionTimeout(30_000);
    ds.setIdleTimeout(600_000);
    ds.setMaxLifetime(1_800_000);
    // Keep-alive query to prevent Railway from closing idle connections
    ds.setKeepaliveTime(60_000);
    ds.setConnectionTestQuery("SELECT 1");
    return ds;
  }

  private String getEnvOrDefault(String key, String defaultValue) {
    String val = System.getenv(key);
    return (val != null && !val.isBlank()) ? val.trim() : defaultValue;
  }
}
