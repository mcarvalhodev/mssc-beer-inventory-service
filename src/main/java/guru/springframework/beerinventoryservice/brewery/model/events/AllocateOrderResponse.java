package guru.springframework.beerinventoryservice.brewery.model.events;

import guru.springframework.beerinventoryservice.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateOrderResponse {

  private boolean allocationError;
  private boolean pendingInventory;
  private BeerOrderDto order;
}
