package gr.bc.api;

import gr.bc.api.controller.CompanyController;
import gr.bc.api.model.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author konstantinos
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CompanyControllerTest {

    @Autowired
    private CompanyController controller;

    @Test
    public void testSaveCompany() {

        Company c = new Company();
        c.setName("Triaps Inc. 2");

        ResponseEntity<Company> response = controller.saveCompany(c, UriComponentsBuilder.newInstance());
        System.out.println(response.getBody().toString());
    }

}
