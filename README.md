# Raspberry House

Projeto em Kotlin/Spring Boot para automação residencial utilizando Raspberry Pi.

Este projeto gerencia a interação com botões físicos conectados aos pinos GPIO do Raspberry Pi e controla LEDs correspondentes através de requisições externas.

## Estrutura do Projeto

O projeto segue uma arquitetura baseada em componentes Spring:

*   **`adapter.input`**: Contém os ouvintes (listeners) e fábricas para os dispositivos de entrada (botões).
*   **`adapter.output`**: Gerencia a comunicação de saída (ex: controle de LEDs).
*   **`domain`**: Contém a lógica de domínio, como o serviço `LedButtonService` que orquestra a associação entre botões e LEDs.

## Funcionalidades

*   **Monitoramento de Botões**: Escuta eventos de pressão em botões físicos conectados aos pinos GPIO.
*   **Controle de LEDs**: Envia comandos para acender/apagar LEDs em resposta aos eventos dos botões.
*   **Configuração Flexível**: Mapeamento fácil entre pinos GPIO e IDs de LEDs.
*   **Ciclo de Vida Gerenciado**: Utiliza anotações do Spring (`@PostConstruct`, `@PreDestroy`) para garantir a correta inicialização e liberação dos recursos de hardware.

## Tecnologias Utilizadas

*   **Kotlin**: Linguagem de programação principal.
*   **Spring Boot**: Framework para injeção de dependência e gerenciamento do ciclo de vida da aplicação.
*   **Diozero**: Biblioteca para interação com a interface GPIO do Raspberry Pi.

## Configuração de Hardware

O projeto está configurado para utilizar os seguintes pinos GPIO (BCM) para os botões:

*   GPIO 17 -> LED 1
*   GPIO 27 -> LED 2
*   GPIO 22 -> LED 3
*   GPIO 5  -> LED 4
*   GPIO 6  -> LED 5
*   GPIO 13 -> LED 6

## Como Executar

1.  Certifique-se de ter o JDK instalado.
2.  Clone o repositório.
3.  Execute o projeto via Maven:

```bash
./mvnw spring-boot:run
```

## Observações

Este projeto foi desenvolvido para ser executado em um ambiente Raspberry Pi. A biblioteca Diozero abstrai o acesso ao hardware, mas a execução em outros ambientes pode exigir configurações adicionais ou simulação dos pinos GPIO.
