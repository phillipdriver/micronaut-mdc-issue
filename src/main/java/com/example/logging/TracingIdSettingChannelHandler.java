package com.example.logging;

import io.micronaut.core.annotation.NonNull;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.UUID;
import org.slf4j.MDC;

public class TracingIdSettingChannelHandler extends ChannelDuplexHandler {
  public static final String ID = "tracing-id-setting-channel-handler";

  @Override
  public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg)
      throws Exception {
    // add tracingId to MDC for traceability
    MDC.put("tracingId", UUID.randomUUID().toString());
    super.channelRead(ctx, msg);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.disconnect(ctx, promise);
    MDC.clear();
  }
}
