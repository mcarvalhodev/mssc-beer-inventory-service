package guru.springframework.beerinventoryservice.services;

import guru.springframework.beerinventoryservice.brewery.model.BeerInventoryDto;
import guru.springframework.beerinventoryservice.domain.BeerInventory;
import guru.springframework.beerinventoryservice.repositories.BeerInventoryRepository;
import guru.springframework.beerinventoryservice.web.mappers.BeerInventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/beer")
public class NewInventoryService {

  private final BeerInventoryRepository repository;
  private final BeerInventoryMapper mapper;

  @PostMapping
  public ResponseEntity<?> save(@RequestBody BeerInventoryDto dto) throws URISyntaxException {
    BeerInventory beerInventory =
        repository.save(
            BeerInventory.builder()
                .beerId(dto.getId())
                .upc(dto.getUpc())
                .quantityOnHand(dto.getQuantityOnHand())
                .build());

    BeerInventoryDto beerInventoryDto = mapper.beerInventoryToBeerInventoryDto(beerInventory);

    return ResponseEntity.created(new URI("api/v1/beer")).body(beerInventoryDto);
  }
}
