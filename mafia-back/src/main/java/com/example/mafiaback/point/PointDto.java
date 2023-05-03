package com.example.mafiaback.point;

import com.example.mafiaback.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PointDto {
  private Integer id;
  private String name;
  private Integer price;
  private Timestamp lastPaymentDate;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private AddressDto address;
  private UserDto client;
  private Integer managerId;
  
  public static PointDto fromPoint(Point point) {
    return PointDto.builder()
        .id(point.getId())
        .name(point.getName())
        .price(point.getPrice())
        .lastPaymentDate(point.getLastPaymentDate())
        .latitude(point.getLatitude())
        .longitude(point.getLongitude())
        .address(AddressDto.fromAddress(point.getAddress()))
        .client(UserDto.fromUser(point.getClient()))
        .managerId(point.getManagerId())
        .build();
  }

  public static Point fromPointDto(PointDto point) {
    return Point.builder()
        .name(point.getName())
        .price(point.getPrice())
        .latitude(point.getLatitude())
        .longitude(point.getLongitude())
        .address(AddressDto.fromAddressDto(point.getAddress()))
        .build();
  }
}
