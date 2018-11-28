import exception.ConfigException;
import model.ConfigSystem;
import model.Processo;

import java.util.*;

public class ExecuteCommand {
  private Map<Integer,Processo> lista = new HashMap<Integer, Processo>();
  private Integer pid = 0;
  private ConfigSystem configuracoes = null;
  Scanner scanner = new Scanner(System.in);
  List<Processo> clusterMemoria = null;
  List<Processo> memoriaInterna = null;
  Double memoriaA = null;

  /**
   * Exibe todos os comandos existentes
   */
  public void comandosExistentesAjuda() {
    System.out.println("Comandos disponíveis:\n" +
            "ajuda - Mostra a lista de comandos disponíveis.\n" +
            "acesso - Simula o acesso à memória do processo.\n" +
            "relatorio - Exibe o estado atual do sistema virtual.\n" +
            "processo - Cria um novo processo no sistema virtual.\n" +
            "terminar - Limpa totalmente o sistema virtual.\n" +
            "configurar - Configura o ambiente um novo ambiente virtual.");
  }

  /**
   * Cria novos processos para serem armazenados na memória
   * @throws Exception
   */
  public void startProcess() throws Exception {
    if(configuracoes != null) {
      pid += 1;
      System.out.print("Novo processo - Memória do processo(bits): ");
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
      if(processo.getMemoria() <= configuracoes.getMemoriaBits()) {
        if((memoriaA - clusterMemoria.size()) > processo.getMemoria()) {
          for (int i = 0; i < processo.getMemoria(); i++) {
            clusterMemoria.add(processo);
          }
        } else {
          for(int i=0; i < processo.getMemoria(); i++) {
            memoriaInterna.add(processo);
          }
        }
      } else {
        throw new Exception("Erro- memória do processo maior que a do sistema");
      }

      System.out.println("Processo criado");
    } else {
      throw new ConfigException("ERRO - Configurar sistema antes de iniciar um processo");
    }
  }

  /**
   * Configura a maquina com quantidade de memoria RAM
   * e quantidade de memoria no HD
   *
   */
  public void configMachine() {

    System.out.println("Configure seu computador");
    System.out.print("Quantida de espaço de memoria RAM(em bits): ");
    memoriaA = scanner.nextDouble();
    clusterMemoria = new ArrayList<Processo>((int) Math.round(memoriaA));
    System.out.print("Quantidade de memória física instalada (HD): ");
    String memoriaFisica = scanner.next();
    int memoriaFisicaValue = 0;
    int value = 0;
    if(!memoriaFisica.contains("x")) {
      memoriaFisicaValue = Integer.parseInt(memoriaFisica);
    } else {
      memoriaFisica = memoriaFisica.substring(2,memoriaFisica.length());
      value = Integer.parseInt(memoriaFisica, 16);
    }
    memoriaInterna = new ArrayList<Processo>(memoriaFisicaValue > 0 ? memoriaFisicaValue : value);
    configuracoes = new ConfigSystem(memoriaA, (memoriaFisicaValue > 0 ? memoriaFisicaValue : value));
    System.out.println("Configurações");
    System.out.println("--------------------------------------------------");
    System.out.println("Memória bits: " + memoriaA);
    System.out.println("Memoria Física: " + (memoriaFisicaValue > 0 ? memoriaFisicaValue : value));
  }

  /**
   * Encontra o processo e mostra qual o endereço de memória que o processo
   * esta alocado
   */
  public void acessoMemoriaFindProcesso() {
    System.out.print("PID do processo: ");
    ArrayList<Integer> endereco = new ArrayList<>();
    int pid = scanner.nextInt();
    for( int i=0 ; i < clusterMemoria.size(); i++) {
      if(clusterMemoria.get(i).getPid() == pid) {
        endereco.add(i);
      }
    }

    if(endereco.size() == 0) {
      for( int i=0 ; i < memoriaInterna.size(); i++) {
        if(memoriaInterna.get(i).getPid() == pid) {
          endereco.add(i);
        }
      }
    }
    System.out.println("           ACESSO A MEMÓRIA          ");
    System.out.println("-------------------------------------");
    System.out.println("Processo acessado\npid: " + pid +
            "\nEndereço de memória: " + endereco.get(0) + "-" + endereco.get(endereco.size()-1));
  }

}
