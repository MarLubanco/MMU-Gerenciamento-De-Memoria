import exception.ConfigException;

public class MMU {

  public static void main(String[] args) throws ConfigException {
    CommandReader commandReader = new CommandReader();
    commandReader.run();
  }
}
