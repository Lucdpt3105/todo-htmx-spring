package com.atoolz.htmx.config;

import com.zaxxer.hikari.HikariDataSource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RailwayDataSourceConfig {

  @Bean
  @Primary
  public DataSource dataSource() {
    String raw = System.getenv("DATABASE_URL");
    if (raw == null || raw.isBlank()) {
      throw new IllegalStateException(
          "DATABASE_URL is required (e.g. reference ${{Postgres.DATABASE_URL}} on Railway)");
    }
    String url = raw.trim();
    if (url.startsWith("postgres://")) {
      url = "postgresql://" + url.substring("postgres://".length());
    }
    if (!url.startsWith("postgresql://")) {
      throw new IllegalArgumentException("DATABASE_URL must start with postgres:// or postgresql://");
    }
    String rest = url.substring("postgresql://".length());
    int at = rest.lastIndexOf('@');
    if (at < 0) {
      throw new IllegalArgumentException("DATABASE_URL missing credentials");
    }
    String userInfo = rest.substring(0, at);
    String hostPart = rest.substring(at + 1);
    int colon = userInfo.indexOf(':');
    String user = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
    String password = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);

    int slash = hostPart.indexOf('/');
    if (slash < 0) {
      throw new IllegalArgumentException("DATABASE_URL missing database name");
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

    StringBuilder jdbc = new StringBuilder();
    jdbc.append("jdbc:postgresql://").append(host).append(":").append(port).append("/").append(database);
    if (!existingQuery.isEmpty()) {
      jdbc.append("?").append(existingQuery);
    } else if (!host.equals("localhost") && !host.equals("127.0.0.1")) {
      jdbc.append("?sslmode=require");
    }

    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(jdbc.toString());
    ds.setUsername(user);
    ds.setPassword(password);
    ds.setMaximumPoolSize(5);
    return ds;
  }
}
