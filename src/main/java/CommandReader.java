import exception.ConfigException;
import model.ConfigSystem;

import java.util.Scanner;

public class CommandReader {

  private Integer pid = 0;
  private ConfigSystem configuracoes = null;
  Scanner scanner = new Scanner(System.in);

  ExecuteCommand command = new ExecuteCommand();

  public void run() throws ConfigException {
    while (true) {
      showDefaultPrompt();
      String comando = scanner.nextLine();
      comandos(comando);
    }
  }

  private void showDefaultPrompt() {
    System.out.print("> ");
  }

  public void comandos(String comando) throws ConfigException {
    if ("configurar".equals(comando)) {
      command.configMachine();
    } else if ("ajuda".equals(comando)) {
      command.comandosExistentesAjuda();
    } else if ("processo".equals(comando)) {
      command.startProcess();
    } else if ("acesso".equals(comando)) {
      System.out.println("Não existe esse comando");
    } else if ("terminar".equals(comando)) {
      System.out.println("Não existe esse comando");
    } else if ("relatorio".equals(comando)) {
      System.out.println("Não existe esse comando");
    } else {
      System.out.println("Não existe esse comando");
    }
  }

  public static int hex2decimal(String s) {
    String digits = "0123456789ABCDEF";
    s = s.toUpperCase();
    int val = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      int d = digits.indexOf(c);
      val = 16*val + d;
    }
    return val;
  }

}
