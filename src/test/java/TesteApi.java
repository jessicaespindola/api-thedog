import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TesteApi extends MassaDeDados {

    @BeforeClass

    public static void ulrBase() {
        RestAssured.baseURI = "https://api.thedogapi.com/v1/";
    }

    @Test
    public void cadastro() {

        Response response = given().
                contentType("application/json").
                body(corpoCadastro).
                when().post(urlCadastro);

        validacao(response);

    }

    @Test
    public void votacao() {

        Response response = given().
                contentType("application/json").
                body("{\"image_id\":\"1w-bkZ4Ky\",\"value\":true,\"sub_id\":\"demo-52145\"}").
                when().
                post(urlVotacao);

        validacao(response);
        String id = response.jsonPath().getString("id");
        vote_id = id;
        System.out.println("ID votação => " + id);

    }

    @Test
    public void deletar() {
        votacao();
        deletaVoto();
    }

    private void deletaVoto() {

        Response response = given().contentType("application/json")
                .header("x-api-key", "e3324f4d-d053-42a7-b96b-130efc1c0b92").pathParam("vote_id", vote_id).when()
                .delete("votes/{vote_id}");
        validacao(response);
    }

    @Test
    public void TesteApiFavoritar() {
        favoritar();
        desfavoritar();
    }

    private void favoritar() {
        Response response = given().
                contentType("application/json").
                header("x-api-key", "87025215-f380-4d4b- 96d5-1aeed50687d2").
                body(corpoFavoritar).
                when().
                post(urlFavoritar);
        String id = response.jsonPath().getString("id");
        vote_id = id;
        validacao(response);
    }

    public void desfavoritar() {
        Response response = given().
                contentType("application/json").
                header("x-api-key", "87025215-f380-4d4b- 96d5-1aeed50687d2").
                pathParam("favourite_id", vote_id).
                when().
                delete("favourites/{favourite_id}");
        validacao(response);
    }

    public void validacao(Response response){
        response.then().body("message", containsString("SUCCESS")).statusCode(200);
        System.out.println("RETORNO API => " + response.body().asString());
        System.out.println("------------------------------------------------------");
    }
}
