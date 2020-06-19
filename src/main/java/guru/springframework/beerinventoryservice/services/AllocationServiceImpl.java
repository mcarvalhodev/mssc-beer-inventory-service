package guru.springframework.beerinventoryservice.services;

import guru.springframework.beerinventoryservice.brewery.model.BeerOrderDto;
import guru.springframework.beerinventoryservice.brewery.model.BeerOrderLineDto;
import guru.springframework.beerinventoryservice.domain.BeerInventory;
import guru.springframework.beerinventoryservice.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNullElse;

@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

  private final BeerInventoryRepository beerInventoryRepository;

  @Override
  public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
    log.debug("Allocating OrderId: " + beerOrderDto.getId());

    AtomicInteger totalOrdered = new AtomicInteger();
    AtomicInteger totalAllocated = new AtomicInteger();

    beerOrderDto
        .getBeerOrderLines()
        .forEach(
            beerOrderLine -> {
              final int i = requireNonNullElse(beerOrderLine.getOrderQuantity(), 0);
              final int j = requireNonNullElse(beerOrderLine.getQuantityAllocated(), 0);

              if ((i - j > 0)) {
                allocateBeerOrderLine(beerOrderLine);
              }

              totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());

              int i1 = requireNonNullElse(beerOrderLine.getQuantityAllocated(), 0);
              totalAllocated.set(totalAllocated.get() + i1);
            });

    log.debug("Total Ordered: " + totalOrdered.get() + " Total Allocated: " + totalAllocated.get());

    return totalOrdered.get() == totalAllocated.get();
  }

  private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {
    List<BeerInventory> beerInventoryList =
        beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc());

    beerInventoryList.forEach(
        beerInventory -> {
          int inventory = requireNonNullElse(beerInventory.getQuantityOnHand(), 0);
          int orderQty = requireNonNullElse(beerOrderLine.getOrderQuantity(), 0);
          int allocatedQty = requireNonNullElse(beerOrderLine.getQuantityAllocated(), 0);

          int qtyToAllocate = orderQty - allocatedQty;

          if (inventory >= qtyToAllocate) { // full allocation
            inventory = inventory - qtyToAllocate;
            beerOrderLine.setQuantityAllocated(orderQty);
            beerInventory.setQuantityOnHand(inventory);

            beerInventoryRepository.save(beerInventory);
          } else if (inventory > 0) { // partial allocation
            beerOrderLine.setQuantityAllocated(allocatedQty + inventory);
            beerInventory.setQuantityOnHand(0);
          }

          if (beerInventory.getQuantityOnHand() == 0) {
            beerInventoryRepository.delete(beerInventory);
          }
        });
  }

  @Override
  public void deallocateOrder(BeerOrderDto beerOrderDto) {
    beerOrderDto
        .getBeerOrderLines()
        .forEach(
            beerOrderLineDto -> {
              BeerInventory beerInventory =
                  BeerInventory.builder()
                      .beerId(beerOrderLineDto.getBeerId())
                      .upc(beerOrderLineDto.getUpc())
                      .quantityOnHand(beerOrderLineDto.getQuantityAllocated())
                      .build();

              BeerInventory savedInventory = beerInventoryRepository.save(beerInventory);

              log.debug(
                  "Saved Inventory for beer upc: "
                      + savedInventory.getUpc()
                      + " inventory id: "
                      + savedInventory.getId());
            });
  }
}
