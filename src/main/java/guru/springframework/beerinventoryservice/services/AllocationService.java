package guru.springframework.beerinventoryservice.services;

import guru.springframework.beerinventoryservice.brewery.model.BeerOrderDto;

public interface AllocationService {

    Boolean allocateOrder(BeerOrderDto order);

    void deallocateOrder(BeerOrderDto order);
}
