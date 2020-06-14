package guru.springframework.beerinventoryservice.web.mappers;

import guru.springframework.beerinventoryservice.brewery.model.BeerInventoryDto;
import guru.springframework.beerinventoryservice.domain.BeerInventory;
import org.mapstruct.Mapper;

/** Created by jt on 2019-05-31. */
@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {

  BeerInventory beerInventoryDtoToBeerInventory(BeerInventoryDto beerInventoryDTO);

  BeerInventoryDto beerInventoryToBeerInventoryDto(BeerInventory beerInventory);
}
