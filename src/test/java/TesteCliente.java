import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;

public class TesteCliente {

    String enderecoApiCliente = "http://localhost:8080/";
    String endpointCliente = "cliente";
    String endpointApagaTodos = "/apagaTodos";
    String listaVazia = "{}";



    @Test
    @DisplayName("Quando eu pegar todos os clientes sem cadastrar, então a lista deve estar vazia")
    public void quandoPegarListaSemCadastrar_EntaoListaDeveSerVazia() {
        deletaTodosClientes();

        given()
                .contentType(ContentType.JSON)

                .when()
                .get(enderecoApiCliente)

                .then()
                .statusCode(SC_OK)
                .assertThat().body(new IsEqual<>(listaVazia));
    }

    @Test
    @DisplayName("Quando cadastrar um cliente, então ele deve estar disponível no resultado")
    public void quandoCadastrarUmCliente_EntaoEleDeveEstarDisponivelNoResultado(){

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1004,\n" +
                "  \"idade\": 29,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String respostaEsperada = "{\"1004\":{\"nome\":\"Minnie Mouse\",\"idade\":29,\"id\":1004,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoApiCliente + endpointCliente)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando alterar um cliente, então ele deve trazer o resultado alterado")
    public void quandoAlterarUmCliente_EntaoDeveImprimirOResultadoAlterado(){

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1004,\n" +
                "  \"idade\": 29,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String clienteAlterado = "{\n" +
                "  \"id\": 1004,\n" +
                "  \"idade\": 35,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"1004\":{\"nome\":\"Minnie Mouse\",\"idade\":35,\"id\":1004,\"risco\":0}}";


        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoApiCliente + endpointCliente)
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        given()
                .contentType(ContentType.JSON)
                .body(clienteAlterado)
                .when()
                .put(enderecoApiCliente + endpointCliente  )
                .then()
                .statusCode(SC_OK)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando deletar um cliente, então ele não deve estar disponível no resultado")
    public void quandoDeletarUmCliente_EntaoEleNaoDeveEstarDisponivelNoResultado(){

        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1004,\n" +
                "  \"idade\": 29,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "CLIENTE REMOVIDO: { NOME: Minnie Mouse, IDADE: 29, ID: 1004 }";
        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(enderecoApiCliente + endpointCliente)
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(enderecoApiCliente  + endpointCliente + "/1004")
                .then()
                .statusCode(SC_OK)
                .body(new IsEqual<>(respostaEsperada));
    }


    //metodo de apoio
    public void deletaTodosClientes(){


        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(enderecoApiCliente  + endpointCliente + endpointApagaTodos)
                .then()
                .statusCode(SC_OK)
                .body(new IsEqual<>(listaVazia));

    }



}






