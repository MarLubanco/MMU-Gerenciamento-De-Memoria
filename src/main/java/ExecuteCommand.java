import exception.ConfigException;
import model.ConfigSystem;
import model.Processo;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExecuteCommand {

  static Logger log = Logger.getLogger(MMU.class.getName());
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
            "ajuda - Mostra a lista de comandos disponíveis (está implementado).\n" +
            "acesso - Simula o acesso à memória do processo (está implementado).\n" +
            "relatorio - Exibe o estado atual do sistema virtual (está implementado).\n" +
            "processo - Cria um novo processo no sistema virtual (está implementado).\n" +
            "terminar - Limpa totalmente o sistema virtual (está implementado).\n" +
            "configurar - Configura o ambiente um novo ambiente virtual (está implementado).");
  }

  /**
   * Cria novos processos para serem armazenados na memória
   * @throws Exception
   */
  public void startProcess() throws Exception {
    if(configuracoes != null) {
      if (configuracoes.getMmu().equalsIgnoreCase("Swapping")) {
        pid += 1;
        System.out.print("Novo processo - Memória do processo(bits): ");
        String memoria = scanner.next();
        int entradaMemoria = 0;
        int value = 0;
        if (!memoria.contains("x")) {
          entradaMemoria = Integer.parseInt(memoria);
        } else {
          memoria = memoria.substring(2, memoria.length());
          value = Integer.parseInt(memoria, 16);
        }
        Processo processo = new Processo(pid, (entradaMemoria > 0 ? entradaMemoria : value));
        if (processo.getMemoria() <= configuracoes.getMemoriaBits()) {
          if ((memoriaA - clusterMemoria.size()) > processo.getMemoria()) {
            for (int i = 0; i < processo.getMemoria(); i++) {
              clusterMemoria.add(processo);
            }
          } else {
            for (int i = 0; i < processo.getMemoria(); i++) {
              memoriaInterna.add(processo);
            }
          }
          log.info("Processo criado");

        } else {
          log.warning("Erro- memória do processo maior que a do sistema");
        }

      } else {
         log.warning("ERRO - Configurar sistema antes de iniciar um processo");
      }
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
    System.out.print("Tipo de MMU: ");
    String mmu = scanner.next();
    int memoriaFisicaValue = 0;
    int value = 0;
    if(!memoriaFisica.contains("x")) {
      memoriaFisicaValue = Integer.parseInt(memoriaFisica);
    } else {
      memoriaFisica = memoriaFisica.substring(2,memoriaFisica.length());
      value = Integer.parseInt(memoriaFisica, 16);
    }
    memoriaInterna = new ArrayList<Processo>(memoriaFisicaValue > 0 ? memoriaFisicaValue : value);
    configuracoes = new ConfigSystem(memoriaA, (memoriaFisicaValue > 0 ? memoriaFisicaValue : value), mmu);
    System.out.println("Configurações");
    System.out.println("--------------------------------------------------");
    System.out.println("Memória RAM (bits): " + memoriaA);
    System.out.println("Memoria Interna (bits): " + (memoriaFisicaValue > 0 ? memoriaFisicaValue : value));
    System.out.println("MMU: " + mmu);
  }

  /**
   * Encontra o processo e mostra qual o endereço de memória que o processo
   * esta alocado
   */
  public void acessoMemoriaFindProcesso() throws Exception {
    System.out.print("PID do processo: ");
    ArrayList<Integer> endereco = new ArrayList<>();
    int pid = scanner.nextInt();
    String local = null;
    for( int i=0 ; i < clusterMemoria.size(); i++) {
      if(clusterMemoria.get(i).getPid() == pid) {
        local = "RAM";
        endereco.add(i);
      }
    }

    if(endereco.size() == 0) {
      for( int i=0 ; i < memoriaInterna.size(); i++) {
        if(memoriaInterna.get(i).getPid() == pid) {
          log.warning("Não tem permissão para acessar esse processo no HD");
        }
      }
    }
    System.out.println("           ACESSO A MEMÓRIA          ");
    System.out.println("-------------------------------------");
    System.out.print("Processo acessado\npid: " + pid);
    System.out.println("\nEndereço de memória " + local + ": " + endereco.get(0) + "-" + endereco.get(endereco.size()-1));
  }

  /**
   * Mostra todos os processos que estão em execução
   */
  public void relatorio() {
    if (clusterMemoria == null) {
      System.out.println("Não existe processos ainda");
    }
    System.out.println("           RELATÓRIO DE PROCESSOS");
    System.out.println("==================================================");
    clusterMemoria.stream()
            .distinct()
            .forEach(processo ->  {
              if (processo.getPid() > 0)
              System.out.println("Memoria Principal - processo - PID: " + processo.getPid() +
                      " tamanho: " + processo.getMemoria());
            });
    if (memoriaInterna != null) {
      memoriaInterna.stream()
              .distinct()
              .forEach(processo -> System.out.println("Memoria Interna - processo - PID: " + processo.getPid() +
                      " tamanho: " + processo.getMemoria()));
    }
  }

  /**
   * Matar um processo que está sendo executado
   */
  public void terminar() {
    System.out.print("Finalizar processo, PID: ");
    int pidKill = scanner.nextInt();
    int tamanho =  clusterMemoria.size();
    clusterMemoria.forEach(processo -> {
      if(processo.getPid() == pidKill) {
        processo.setPid(0);
        processo.setMemoria(0);
      }
    });
//    for (int i=0; i <= clusterMemoria.size(); i++) {
//
//    }
    System.out.println("Processo " + pidKill + " finalizado");
  }

}
