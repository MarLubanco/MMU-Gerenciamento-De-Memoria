import exception.ConfigException;
import model.ConfigSystem;
import model.Processo;

import java.util.Scanner;

public class ExecuteCommand {

  private Integer pid = 0;
  private ConfigSystem configuracoes = null;
  Scanner scanner = new Scanner(System.in);

  public void comandosExistentesAjuda() {
    System.out.println("Comandos disponíveis:\n" +
            "ajuda - Mostra a lista de comandos disponíveis.\n" +
            "acesso - Simula o acesso à memória do processo.\n" +
            "relatorio - Exibe o estado atual do sistema virtual.\n" +
            "processo - Cria um novo processo no sistema virtual.\n" +
            "terminar - Limpa totalmente o sistema virtual.\n" +
            "configurar - Configura o ambiente um novo ambiente virtual.");
  }

  public void startProcess() throws ConfigException {
    if(configuracoes != null) {
      pid += 1;
      System.out.print("Novo processo - Memória do processo: ");
      String memoria = scanner.next();
      int entradaMemoria = 0;
      int value = 0;
      if(!memoria.contains("x")) {
        entradaMemoria = Integer.parseInt(memoria);
      } else {
        memoria = memoria.substring(2,memoria.length());
        value = Integer.parseInt(memoria, 16);
      }
      Processo processo = new Processo(pid, (entradaMemoria > 0 ? entradaMemoria : value));
      System.out.println("Processo criado");
    } else {
      throw new ConfigException("ERRO - Configurar sistema antes de iniciar um processo");
    }
  }

  public void configMachine() {

    System.out.println("Configure seu computador");
    System.out.print("Quantida de espaço de memoria(em bits): ");
    Double memoria = scanner.nextDouble();
    System.out.print("Quantidade de memória física instalada: ");
    String memoriaFisica = scanner.next();
    int memoriaFisicaValue = 0;
    int value = 0;
    if(!memoriaFisica.contains("x")) {
      memoriaFisicaValue = Integer.parseInt(memoriaFisica);
    } else {
      memoriaFisica = memoriaFisica.substring(2,memoriaFisica.length());
      value = Integer.parseInt(memoriaFisica, 16);
    }
    configuracoes = new ConfigSystem(memoria, (memoriaFisicaValue > 0 ? memoriaFisicaValue : value));
    System.out.println("Configurações");
    System.out.println("--------------------------------------------------");
    System.out.println("Memória bits: " + memoria);
    System.out.println("Memoria Física: " + (memoriaFisicaValue > 0 ? memoriaFisicaValue : value));
  }

}
