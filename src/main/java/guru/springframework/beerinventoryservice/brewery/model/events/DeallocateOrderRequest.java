package guru.springframework.beerinventoryservice.brewery.model.events;

import guru.springframework.beerinventoryservice.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateOrderRequest {

  private BeerOrderDto order;
}
