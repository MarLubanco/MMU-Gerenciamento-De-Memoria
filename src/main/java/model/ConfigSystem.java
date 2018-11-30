package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigSystem {

  private Double memoriaBits;

  private Integer memoriaFisica;

  private String mmu;
}
