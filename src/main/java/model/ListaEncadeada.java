package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListaEncadeada {

  private Integer id;

  private Double tamahoTotal;

  private Map<Integer,Processo> lista = new HashMap<Integer, Processo>();


}
