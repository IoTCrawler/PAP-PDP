package XACMLServletPDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.sun.xacml.simplepdp.*;

import es.odins.blockchain.Blockchain;


/**
 * Servlet implementation class XACMLServlet
 */
@WebServlet("/")
public class XACMLServletPDP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private static org.apache.log4j.Logger log = Logger.getLogger(XACMLServletPDP.class);

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XACMLServletPDP() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("Received a GET from " + request.getRemoteAddr());
		log.error("You have to send a POST message with the XACML Request");
		log.info("==================");

		response.getWriter().append("You have to send a POST message with the XACML Request");

	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("Received a POST from " + request.getRemoteAddr());
		log.info("==================");
		log.info("Request: ");
		log.info("==================");
				String postBody = getBody(request);
		log.info(postBody);	
		log.info("==================");

		
		ServletContext context = getServletContext();
		String fullPath = context.getRealPath("/WEB-INF/config/configPDP.txt");
		String policyFile = readFile(fullPath, StandardCharsets.UTF_8);
        System.out.println(policyFile);
        String result = null;

		String BC_Int = (System.getenv("BlockChain_integration") != null) ? System.getenv("BlockChain_integration") : "0";

		if(BC_Int.equals("1")) {

			boolean sameHash =  false;
			Blockchain bc = new Blockchain();
			System.out.println("PolicyFile: " + policyFile);
			sameHash = bc.isSameHash(policyFile);
			System.out.println("Is the same hash: " + sameHash);
			
			if(sameHash == false) {
				log.info("==================");
				log.info("Response: ");
				log.info("==================");
					response.getWriter().append("Deny: the Hash does not correspond to the one on the blockchain");
				log.info(result);
				log.info("==================");
				return;
			}
		}
		
		try {
        	result = SimplePDP_TEST.test(policyFile, postBody);
		
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			log.info("==================");
			log.info("Response: ");
			log.info("==================");
				response.getWriter().append(result);
			log.info(result);
			log.info("==================");
	
	}

	static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
	
	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}
}
