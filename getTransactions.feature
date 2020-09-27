Feature: Validating all the Get Transactions

##Scenario 1
#@BuildValidation
#Scenario: Retrieve Non P2P claims using getTransactionsAPI
#
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Non P2P"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
#
##Scenario 2	
#@BuildValidation	
#Scenario: Retrieve Non-Cost share claims using getTransactionsAPI
#	
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Non Cost share"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
#
##Scenario 3	
#@BuildValidation
#Scenario: Retrieve Common family[CF2/CF3] claims using getTransactionsAPI
#	
#	Given "Get" Accum Transaction Payload from CSV getFlow for "CF2/CF3"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
##	And "ErrorDataParameters.ErrorCode" in response body of getFlow is "OK"
##	And "TranslationItemList.Member Code" in response body of getFlow is "10"
#
##Scenario 4
#@BuildValidation
#Scenario: Retrieve Commingled claims using getTransactionsAPI
#
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Commingled"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
##
##Scenario 5
#@BuildValidation
#Scenario: Retrieve Dual Update claims using getTransactionsAPI
#	
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Dual Update"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
#	
##Scenario 6
#@BuildValidation
#Scenario: Retrieve member tier claims using getTransactionsAPI
#	
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Member Tier"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
#
##Scenario 7
#@BuildValidation
#Scenario: Retrieve Cross Application claims using getTransactionsAPI
#	
#	Given "Get" Accum Transaction Payload from CSV getFlow for "Cross App"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"

#Scenario 8
@BuildValidation
Scenario: Retrieve CY-to-CY claims using getTransactionsAPI

	Given "Get" Accum Transaction Payload from CSV getFlow for "CY TO CY"
	When user calls "GetTransaction" with "Post" http request getFlow
	Then the API call got success with status code 200 getFlow
	And "ServiceStatus" in response body of getFlow is "OK"
	And "Amount" in response body matches with expected amount in csv file
	
##Scenario 9
#Scenario: Retrieve Amount using getTransactionsAPI
#
#	Given "Get" Accum Transaction Payload from CSV getFlow for "BY TO BY"
#	When user calls "GetTransaction" with "Post" http request getFlow
#	Then the API call got success with status code 200 getFlow
#	And "ServiceStatus" in response body of getFlow is "OK"
