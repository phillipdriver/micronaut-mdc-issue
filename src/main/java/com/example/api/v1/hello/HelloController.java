package com.example.api.v1.hello;

import com.example.client.HelloClient;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/v1/user")
public class HelloController
{
  private final HelloClient client;

  public HelloController(HelloClient client)
  {
    this.client = client;
  }

  @Get("/{id}/hello")
  public String hello(String id)
  {
    String userName = client.fetchUserNameById(id);

    return "Hello " + userName + "!";
  }
}
