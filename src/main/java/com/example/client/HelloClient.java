package com.example.client;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client(id = "some-service")
public interface HelloClient {

  @Get("/some-service/user/{id}/name")
  @Consumes(MediaType.TEXT_PLAIN)
  String fetchUserNameById(String id);
}
