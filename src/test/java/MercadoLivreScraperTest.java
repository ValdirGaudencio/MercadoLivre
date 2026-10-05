import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class MercadoLivreScraperTest {

    @Test
    public void buscarProdutoEExportarCSV() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        try {

            // 1. Acessar o Mercado Livre
            driver.get("https://www.mercadolivre.com.br/");

            WebDriverWait wait = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(15)
            );

            // 2. Localizar o campo de pesquisa
            WebElement campoBusca = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.name("as_word")
                    )
            );

            // Digitar o produto
            campoBusca.sendKeys("Xiaomi POCO");

            // Pressionar ENTER
            campoBusca.sendKeys(Keys.ENTER);

            // 3. Aguardar o primeiro produto
            WebElement primeiroProduto = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("li.ui-search-layout__item")
                    )
            );

            // 4. Extrair a descrição
            WebElement tituloElemento = primeiroProduto.findElement(
                    By.cssSelector("a.poly-component__title")
            );

            String descricao = tituloElemento.getText();

            // 5. Extrair o link
            String url = tituloElemento.getAttribute("href");

            // 6. Extrair o preço
            WebElement precoElemento = primeiroProduto.findElement(
                    By.cssSelector("span.andes-money-amount__fraction")
            );

            String preco = precoElemento.getText();

            // Mostrar os dados no console
            System.out.println("Descrição: " + descricao);
            System.out.println("Preço: " + preco);
            System.out.println("URL: " + url);

            // 7. Criar o arquivo CSV
            try (FileWriter writer = new FileWriter("produto.csv")) {

                writer.append("Descrição,Preço,URL\n");

                writer.append("\"")
                        .append(descricao.replace("\"", "\"\""))
                        .append("\",\"")
                        .append(preco.replace("\"", "\"\""))
                        .append("\",\"")
                        .append(url.replace("\"", "\"\""))
                        .append("\"\n");
            }

            System.out.println("Arquivo produto.csv criado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}