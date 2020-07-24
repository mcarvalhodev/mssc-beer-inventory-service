package guru.springframework.beerinventoryservice.services.listener;

import guru.springframework.beerinventoryservice.brewery.model.BeerOrderDto;
import guru.springframework.beerinventoryservice.brewery.model.events.AllocateOrderRequest;
import guru.springframework.beerinventoryservice.brewery.model.events.AllocateOrderResponse;
import guru.springframework.beerinventoryservice.config.JmsConfig;
import guru.springframework.beerinventoryservice.services.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderAllocationListener implements MessageListener<AllocateOrderRequest> {

  private final AllocationService allocationService;
  private final JmsTemplate jmsTemplate;

  @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
  @Override
  public void listen(AllocateOrderRequest payload) {
    final BeerOrderDto order = payload.getOrder();

    final AllocateOrderResponse.AllocateOrderResponseBuilder builder =
        AllocateOrderResponse.builder().order(order);

    try {
      final Boolean result = allocationService.allocateOrder(order);
      if (result) {
        builder.pendingInventory(false);
      } else {
        builder.pendingInventory(true);
      }

      builder.allocationError(false);
    } catch (Exception e) {
      log.error("Allocation failed for Order[id=" + order.getId() + "]");
      builder.allocationError(true);
    }

    jmsTemplate.convertAndSend(
        JmsConfig.ALLOCATE_ORDER_RESPONSE, MessageBuilder.withPayload(builder.build()));
  }
}
