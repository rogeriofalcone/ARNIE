/**
 * Created by gregorlenz on 16/09/15.
 */


import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringTestSupport
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.context.support.AbstractXmlApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners([DependencyInjectionTestExecutionListener.class])
@ContextConfiguration(locations = ["/context.xml"])
class generateMessages extends CamelSpringTestSupport{

    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/context.xml");
    }


    @Override
    public String isMockEndpointsAndSkip(){
        return "((direct:error)|(direct:admit)|(direct:transfer)|(direct:discharge)|(direct:updatePatient)|(direct:visitUpdate))";
    }


    @Test
    public void testA08() throws InterruptedException, IOException {
        Resource input = new ClassPathResource("/msg-08.hl7");

        HapiContext hapiContext = new DefaultHapiContext()
        def A02 = Message.ADT_A02('2.4')


        MockEndpoint visitUpdateEndpoint = getMockEndpoint("mock:direct:visitUpdate");
        visitUpdateEndpoint.expectedMessageCount(1);

        MockEndpoint admitEndpoint = getMockEndpoint("mock:direct:admit");
        admitEndpoint.expectedMessageCount(0);

        MockEndpoint transferEndpoint = getMockEndpoint("mock:direct:transfer");
        transferEndpoint.expectedMessageCount(0);

        template.sendBody("direct:hl7listener", input.getInputStream());
        assertMockEndpointsSatisfied();
    }

}