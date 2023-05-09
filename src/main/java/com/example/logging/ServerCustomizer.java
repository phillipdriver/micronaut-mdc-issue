package com.example.logging;

import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.netty.channel.ChannelPipelineCustomizer;
import io.micronaut.http.server.netty.NettyServerCustomizer;
import io.netty.channel.Channel;
import jakarta.inject.Singleton;

@Singleton
public class ServerCustomizer implements BeanCreatedEventListener<NettyServerCustomizer.Registry> {
  @Override
  public NettyServerCustomizer.Registry onCreated(
      BeanCreatedEvent<NettyServerCustomizer.Registry> event) {
    NettyServerCustomizer.Registry registry = event.getBean();
    registry.register(new Customizer(null));
    return registry;
  }

  private record Customizer(Channel channel) implements NettyServerCustomizer {
    @NonNull
    @Override
    public NettyServerCustomizer specializeForChannel(@NonNull Channel channel, @NonNull ChannelRole role) {
      return new Customizer(channel);
    }

    @Override
    public void onStreamPipelineBuilt() {
      channel
          .pipeline()
          .addBefore(
              ChannelPipelineCustomizer.HANDLER_HTTP_STREAM,
              TracingIdSettingChannelHandler.ID,
              new TracingIdSettingChannelHandler());
    }
  }
}
