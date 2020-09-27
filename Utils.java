package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.AddTransaction;
import pojo.GetRequest;
import pojo.GetTransaction;
import pojo.JsonResponseData;
//import pojo.JsonResponseData;


public class Utils {
public static JsonResponseData JRD;
	public static RequestSpecification req;
	String baseURLPropertyFile;
	static int j;
	String fileName;
	static ArrayList<JsonResponseData> ResponseData = new ArrayList<JsonResponseData>();
	
	public RequestSpecification requestSpecification(String requestType) throws IOException {
		if (requestType.equals("Add")) {
			baseURLPropertyFile = "baseUrlAdd";
		} else if (requestType.equals("Get")) {
			baseURLPropertyFile = "baseUrlGet";
		} else if (requestType.equalsIgnoreCase("Update")) {
			baseURLPropertyFile = "baseUrlUpdate";
		} else if (requestType.equalsIgnoreCase("Delete")) {
			baseURLPropertyFile = "baseUrlDelete";
		} else {
			System.out.println("Please provide request type in your feature file given");
		}
		
		System.out.println("Value"+baseURLPropertyFile);
		
			//DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss").format(LocalDate.now());
			String dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());
			String fileName=getGlobalValue("logFile")+"eventLog-" + dateFormat + ".log";
			PrintStream log = new PrintStream(new FileOutputStream(fileName, true));
			System.out.println("***Invoked URL***"+getGlobalValue(baseURLPropertyFile));
			req = new RequestSpecBuilder().setBaseUri(getGlobalValue(baseURLPropertyFile))
					.addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log)).setContentType(ContentType.JSON).build();
			return req;
		
		
} 

	public static String getGlobalValue(String key) throws IOException {
		Properties prop = new Properties(); 
        String propertFilePath = System.getProperty("user.dir")+"\\src\\test\\java\\resources\\global.properties";
		FileInputStream fis = new FileInputStream(propertFilePath);
		
		prop.load(fis);
		System.out.println("Value for KEY"+key+ " is ====>"+prop.getProperty(key));
		return prop.getProperty(key);
	}

	/*
	 * Utility which will give any key value in json
	 */
	public String getJsonPath(Response response, String key) {
		String value = null;
		String resp = response.asString();
		
	try {
		JsonPath js = new JsonPath(resp);
		
		value=js.get(key);
		
	}
//	catch(HttpStatusCodeException e) {
//		System.out.println("Exception : "+e);
//	}
	catch(Exception e) {
		System.out.println("Exception : "+e);
	}
		
		return value;
	}
	
	public List getArrayFromJsonPath(Response response, String key) {
	    
	    JsonPath jsonPath = new JsonPath(response.asString());
	    
	    return jsonPath.getList(key);
	}
	
	public Map<String, BigDecimal> getAmountOfAllAccumFromJsonPath(Response response) {
        
	    JsonPath jsonPath = new JsonPath(response.asString());
        int noOfTranslationItemList = jsonPath.getInt("TranslationItemList.size()");
        
        Map<String, BigDecimal> amountPerAccumtMap = new HashMap<String, BigDecimal>();
        
        for(int i=0; i< noOfTranslationItemList; i++) {
            amountPerAccumtMap.put(jsonPath.get("TranslationItemList["+i+"].ClassicAccumName"), jsonPath.get("TranslationItemList["+i+"].Amount"));
        }
        return amountPerAccumtMap; 
    }
	
	public static <T> List<T> csvToObject(String csvPath,Class pojoClass) {
		List<T> data = null;
		File file = new File(csvPath);
        // validate file
        if (file.exists()) {
            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

                // create csv bean reader
                CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                        .withType(pojoClass)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
              
                data = csvToBean.parse();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
		return data;
	}
	
	public static GetRequest createGetRequest(GetTransaction summary) throws IOException {
		GetRequest request = new GetRequest();
		request.setAccumName(summary.getAccumName());
		request.setClaimNetwork(summary.getClaimNetwork());
		request.setMemberCase(summary.getMemberCase());
		request.setMemberCode(summary.getMemberCode());
		request.setMemberContract(summary.getMemberContract());
		request.setMemberHCID(summary.getMemberHCID());
		request.setMemberTier(summary.getMemberTier());
		request.setAccumStartDate(summary.getAccumStartDate());
		request.setAccumEndDate(summary.getAccumEndDate());
		// request.setOrganizationCode(summary.getOrganizationCode());
		request.setOrganizationCode(getGlobalValue("orgCode"));
		request.setServiceStartDate(summary.getServiceStartDate());
		request.setServiceEndDate(summary.getServiceEndDate());
		// transaction.setOrganizationCode(getGlobalValue("orgCode"));
		request.setProviderTaxId(summary.getProviderTaxId());
		request.setDiagnosisCode(summary.getDiagnosisCode());
		request.setMemberTier(summary.getMemberTier());
		return request;
	}
	
	public static void responseForConsole(String consoleResponse) {
		
		JsonPath jsonPath=new JsonPath(consoleResponse);
		
		//if(JSP.get("status").equals(200)) {
			
			int noOf_TranslationItemList=jsonPath.getInt("TranslationItemList.size()");
			System.out.println("no_Of_TranslationItemList"+noOf_TranslationItemList);
			
			String executionTime =jsonPath.get("ExecutionTimeData.executionTime").toString();			
			System.out.println("executionTime = " + executionTime );
			
			String serviceStatus =jsonPath.get("ServiceStatus").toString();			
			System.out.println("serviceStatus = " + serviceStatus );
			
			String errorCode =jsonPath.get("ErrorDataParameters.ErrorCode").toString();			
			System.out.println("errorCode = " + errorCode );
			
			for (int i = 0; i < noOf_TranslationItemList; i++) {
				
				String ClassicAccumName=jsonPath.get("TranslationItemList["+i+"].ClassicAccumName");
				System.out.println("ClassicAccumName = " + ClassicAccumName );
				
				//if(ClassicAccumName.equals("FOOPPBOC")){
					
					String MemberHCId=jsonPath.get("TranslationItemList["+i+"].MemberHCId");
					System.out.println("MemberHCId = " + MemberHCId );
					
					String MemberCode=jsonPath.get("TranslationItemList["+i+"].MemberCode");			
					System.out.println("MemberCode = " + MemberCode );
					
					String MemberCase=jsonPath.get("TranslationItemList["+i+"].MemberCase");
					System.out.println("MemberCase = " + MemberCase );
					
					String MemberContract=jsonPath.get("TranslationItemList["+i+"].MemberContract");
					System.out.println("MemberContract = " + MemberContract );
					
					float Amount=jsonPath.get("TranslationItemList["+i+"].Amount");
					System.out.println("Amount = " + Amount );
					
					int Days=jsonPath.get("TranslationItemList["+i+"].Days");
					System.out.println("Days = " + Days );
					
					int Visits=jsonPath.get("TranslationItemList["+i+"].Visits");
					System.out.println("Visits = " + Visits );
									
					String ClaimNetwork=jsonPath.get("TranslationItemList["+i+"].ClaimNetwork");
					System.out.println("ClaimNetwork = " + ClaimNetwork );
					
						JRD=new JsonResponseData();
						JRD.MemberHCId=MemberHCId;
						JRD.MemberCode=MemberCode;
						JRD.MemberCase=MemberCase;
						JRD.MemberContract=MemberContract;
						JRD.amount=Amount;
						JRD.Days=Days;
						JRD.Visits=Visits;
						JRD.ClassicAccumName=ClassicAccumName;
						JRD.ClaimNetwork=ClaimNetwork;
						ResponseData.add(JRD);
					}
       		}
}