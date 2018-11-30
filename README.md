# Gerenciador de Memória - MMU

## Alunos: Marcelo Lubanco, Jenyffer

### Esse projeto visa mostrar como é feito o gerenciamento de memória em um sistema operacional 
```
 É possível selecionar dois tipos de MMU: Swapping ou Memória Virtual
Swapping : Faz o gerenciamento de processos entre a memória principal e a memória fisíca
Memória Virtual : Gerência o processo referênciando na memória principal o índice da página que o processo está de verdade.
```

### Processos
```
Os processos tem entrada de dados em decimal ou em hexadecimal, vai ser gerenciado de acordo com a MMU selecionada.
```

### Métodos 
```
Ajuda: Mostra todos os métodos implementados.
Configurar: Configura a máquina que tem entradas de memória principal, memória interna e tipo de MMU
Processo: Cria um processo, podendo ter entrada em decimal ou hexadecimal.
Acesso: Varia de acordo com a MMU gerenciado, mostra detalhes do processo que foi selecionado a partir do PID
Terminar: Encerra o processo seleciona a partir do PID selecionado.
Relatório: Mostra todos os processos em andamento de acordo com a MMU swapping ou da Memória Virtual.
```

### Executar projeto
``` ```
### As dependência são gerênciadas pelo maven no arquivo:
 ``` pom.xml```
