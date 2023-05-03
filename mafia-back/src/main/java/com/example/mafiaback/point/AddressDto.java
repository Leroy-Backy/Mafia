package com.example.mafiaback.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
  private String city;
  private String district;
  private String street;
  private String houseNumber;
  
  public static AddressDto fromAddress(Address address) {
    return AddressDto.builder()
        .city(address.getCity())
        .district(address.getDistrict())
        .street(address.getStreet())
        .houseNumber(address.getHouseNumber())
        .build();
  }

  public static Address fromAddressDto(AddressDto address) {
    return Address.builder()
        .city(address.getCity())
        .district(address.getDistrict())
        .street(address.getStreet())
        .houseNumber(address.getHouseNumber())
        .build();
  }
}
