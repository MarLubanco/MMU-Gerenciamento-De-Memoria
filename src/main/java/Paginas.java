import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Processo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paginas {

  private Integer id;

  private Processo processos;

}
