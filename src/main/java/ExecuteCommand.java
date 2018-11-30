import model.ConfigSystem;
import model.Processo;

import java.util.*;
import java.util.logging.Logger;

public class ExecuteCommand {

  static Logger log = Logger.getLogger(MMU.class.getName());
  private Map<Integer, Processo> lista = new HashMap<Integer, Processo>();
  private Integer pid = 0;
  private ConfigSystem configuracoes = null;
  Scanner scanner = new Scanner(System.in);
  List<Processo> clusterMemoria = null;
  List<Processo> memoriaInterna = null;
  List<MemoriaPrincipal> memoriaPrincipals = null;
  List<Processo> paginas = null;
  Double memoriaA = null;
  int pidMemoria = 0;

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
   *
   * @throws Exception
   */
  public void startProcess() throws Exception {
    if (configuracoes != null) {
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
      if (configuracoes.getMmu().equalsIgnoreCase("Swapping")) {

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

      } else if(configuracoes.getMmu().equalsIgnoreCase("MemoriaVirtual")) {
          pidMemoria += 1;
          int pidPag = (int) (Math.random() * ((1000 - 1) + 1));
          Processo processoNovo = new Processo(pidPag, (entradaMemoria > 0 ? entradaMemoria : value));
          MemoriaPrincipal memoriaPrincipal = new MemoriaPrincipal(pidMemoria,pidPag);
          paginas.add(processoNovo);
          memoriaPrincipals.add(memoriaPrincipal);
        log.info("Processo criado");
      } else {
        log.warning("ERRO - Configurar sistema antes de iniciar um processo");
      }
    }
  }

  /**
   * Configura a maquina com quantidade de memoria RAM
   * e quantidade de memoria no HD
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
    if (!memoriaFisica.contains("x")) {
      memoriaFisicaValue = Integer.parseInt(memoriaFisica);
    } else {
      memoriaFisica = memoriaFisica.substring(2, memoriaFisica.length());
      value = Integer.parseInt(memoriaFisica, 16);
    }
    if(mmu.equalsIgnoreCase("swapping")) {
      memoriaInterna = new ArrayList<Processo>(memoriaFisicaValue > 0 ? memoriaFisicaValue : value);
      configuracoes = new ConfigSystem(memoriaA, (memoriaFisicaValue > 0 ? memoriaFisicaValue : value), mmu);
    } else if (mmu.equalsIgnoreCase("memoriavirtual")) {
      paginas = new ArrayList<Processo>(memoriaFisicaValue > 0 ? memoriaFisicaValue : value);
      memoriaPrincipals = new ArrayList<>(memoriaFisicaValue > 0 ? memoriaFisicaValue : value);
      configuracoes = new ConfigSystem(memoriaA, (memoriaFisicaValue > 0 ? memoriaFisicaValue : value), mmu);
    }
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
    if (configuracoes.getMmu().equalsIgnoreCase("swapping")) {
      System.out.print("PID do processo: ");
      ArrayList<Integer> endereco = new ArrayList<>();
      int pid = scanner.nextInt();
      String local = null;
      for (int i = 0; i < clusterMemoria.size(); i++) {
        if (clusterMemoria.get(i).getPid() == pid) {
          local = "RAM";
          endereco.add(i);
        }
      }

      boolean interno = false;
      if (endereco.size() == 0) {
        for (int i = 0; i < memoriaInterna.size(); i++) {
          if (memoriaInterna.get(i).getPid() == pid) {
            interno = true;
          }
        }
        if (interno) {
          log.warning("Não tem permissão para acessar esse processo no HD");
        }
      }

      if (!interno) {
        System.out.println("           ACESSO A MEMÓRIA          ");
        System.out.println("-------------------------------------");
        System.out.print("Processo acessado\npid: " + pid);
        System.out.println("\nEndereço de memória " + local + ": " + endereco.get(0) + "-" + endereco.get(endereco.size() - 1));
      }
    } else {
      System.out.println("Número do índice da página do processo: ");
      int numberPag = scanner.nextInt();
      paginas.forEach(pagina -> {
        if (pagina.getPid() == numberPag) {
          System.out.println("Processo PID: " + pagina.getPid() + " - Memória do processo " + pagina.getMemoria());
        }
      });
    }
  }

  /**
   * Mostra todos os processos que estão em execução
   */
  public void relatorio() {
    if (clusterMemoria == null) {
      System.out.println("Não existe processos ainda");
    }
    if(configuracoes.getMmu().equalsIgnoreCase("swapping")) {
      System.out.println("           RELATÓRIO DE PROCESSOS");
      System.out.println("==================================================");
      clusterMemoria.stream()
              .distinct()
              .forEach(processo -> {
                if (processo.getPid() > 0)
                  System.out.println("Memoria Principal - processo - PID: " + processo.getPid() +
                          " tamanho: " + processo.getMemoria());
              });
      if (memoriaInterna != null) {
        memoriaInterna.stream()
                .distinct()
                .forEach(processo -> {
                  if (processo.getPid() > 0)
                    System.out.println("Memoria Interna - processo - PID: " + processo.getPid() +
                            " tamanho: " + processo.getMemoria());
                });
      }
    } else {
      System.out.println("              RELATORIO DE MEMORIA VIRTUAL");
      System.out.println("==========================================================");
      memoriaPrincipals.stream()
              .distinct()
              .forEach(processo -> {
                if (processo.getId() > 0)
                  System.out.println("Memoria Vitual - Indice memória principal:  "
                          + processo.getId() + " Referencia á pagina: " + processo.getFkPag());
              });
    }
  }

  /**
   * Matar um processo que está sendo executado
   */
  public void terminar() {
    if (configuracoes.getMmu().equalsIgnoreCase("swapping")) {
      System.out.print("Finalizar processo, PID: ");
      int pidKill = scanner.nextInt();
      boolean deletado = false;
      int tamanho = clusterMemoria.size();
      clusterMemoria.forEach(processo -> {
        if (processo.getPid() == pidKill) {
          processo.setPid(0);
          processo.setMemoria(0);
        }
      });

      memoriaInterna.forEach(processo -> {
        if (processo.getPid() == pidKill) {
          processo.setPid(0);
          processo.setMemoria(0);
        }
      });

      System.out.println("Processo " + pidKill + " finalizado");
    } else {
      System.out.print("PID do processo da pagina: ");
      int pidProcess = scanner.nextInt();
      for(int i=0; i < paginas.size(); i++) {
        if (paginas.get(i).getPid() == pidProcess) {
          paginas.remove(paginas.get(i));
        }

      }
      for(int j =0; j < memoriaPrincipals.size(); j++) {
        if(memoriaPrincipals.get(j).getFkPag() == pidProcess) {
          memoriaPrincipals.remove(memoriaPrincipals.get(j));
        }
        System.out.println("Processo finalizado");
      }
    }
  }
}
