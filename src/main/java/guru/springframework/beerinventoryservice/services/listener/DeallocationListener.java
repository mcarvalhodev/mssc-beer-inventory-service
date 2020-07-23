package guru.springframework.beerinventoryservice.services.listener;

import guru.springframework.beerinventoryservice.brewery.model.events.DeallocateOrderRequest;
import guru.springframework.beerinventoryservice.config.JmsConfig;
import guru.springframework.beerinventoryservice.services.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeallocationListener {

  private final AllocationService allocationService;

  @JmsListener(destination = JmsConfig.DEALLOCATE_ORDER_QUEUE)
  public void listen(DeallocateOrderRequest request) {
    allocationService.deallocateOrder(request.getOrder());
  }
}
