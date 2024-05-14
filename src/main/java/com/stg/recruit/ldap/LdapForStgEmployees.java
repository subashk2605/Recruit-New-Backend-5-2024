//package com.stg.recruit.ldap;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.naming.NamingEnumeration;
//import javax.naming.NamingException;
//import javax.naming.directory.Attribute;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.SearchControls;
//import javax.naming.directory.SearchResult;
//import javax.naming.ldap.Control;
//import javax.naming.ldap.InitialLdapContext;
//import javax.naming.ldap.LdapContext;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.stg.recruit.entity.STGEmployee;
//import com.stg.recruit.repository.EmployeeRepository;
//
//@Service
//public class LdapForStgEmployees {
//
//	@Autowired
//	private EmployeeRepository employeeRepository;
//
//	public Set<STGEmployee> getUserDetails() {
//
//		LdapContext ldapContext = getLdapContext();
//
//		SearchControls searchControls = getSearchControls();
//
//		Set<STGEmployee> employees = getAllUsersInfo(ldapContext, searchControls);
////	System.out.println(employees.toString());
//		employeeRepository.saveAll(employees);
////	employeeRepository.findAll();
//		return employees;
//
//	}
//
//	private static LdapContext getLdapContext() {
//
//		InitialLdapContext ctx = null;
//
//		try {
//
//			Hashtable<String, String> env = new Hashtable<String, String>();
//
//			env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
//
//			env.put("java.naming.security.authentication", "Simple");
//
//			env.put("java.naming.security.principal", "CN=Board Room,OU=ITIS,dc=stg,dc=com");
//
//			env.put("java.naming.security.credentials", "Reset123");
//
//			env.put("java.naming.provider.url", "ldap://stg.com:3268/DC=stg,DC=com");
//
//			ctx = new InitialLdapContext(env, (Control[]) null);
//
//			System.out.println("LDAP Connection: COMPLETE");
//
//		} catch (NamingException var2) {
//
//			System.out.println("LDAP Connection: FAILED");
//
//			var2.printStackTrace();
//
//		}
//
//		return ctx;
//
//	}
//
//	private Set<STGEmployee> getAllUsersInfo(LdapContext ctx, SearchControls searchControls) {
//
////		System.out.println("*** " + userName + " ***");
//
//		// ldapContext.search("ldap://stg.com:3268/ou=STG
//		// India,dc=stg,dc=com","(sAMAccountName=*)" , searchControls);
//
//		try {
//
////			NamingEnumeration<SearchResult> answer = ctx.search("ldap://stg.com:3268/dc=stg,dc=com",
////
////					"sAMAccountName=" + userName, searchControls);
//
//			NamingEnumeration<SearchResult> answer = ctx.search("ldap://stg.com:3268/ou=STG India,dc=stg,dc=com",
//					"(sAMAccountName=*)", searchControls);
//
//			List<Map<String, Object>> allEmployee = new ArrayList<Map<String, Object>>();
//
//			Set<STGEmployee> allEmployees = new HashSet<STGEmployee>();
//			System.err.println(answer.toString());
//
//			while (answer.hasMore()) {
//				Attributes attrs = answer.next().getAttributes();
//				System.out.println(attrs.getAll().toString());
//				Map<String, Object> data = new HashMap<String, Object>();
//
//				String[] attributeNames = { "givenname", "sn", "employeeID", "title", "sAMAccountName" };
//
//				STGEmployee employee = new STGEmployee();
//
//				Attribute designationAttribute = attrs.get("title");
//				if (designationAttribute != null) {
//					String designation = designationAttribute.get().toString();
//
//					if (designation.contains("HR") || designation.contains("Manager") ) {
//
//						for (String attributeName : attributeNames) {
//
//							Attribute attribute = attrs.get(attributeName);
//							if (attribute != null) {
//								try {
//									String attributeValue = attribute.get().toString();
//
//									if (attributeName.equals("givenname")) {
//										employee.setEmployeeFirstName(attributeValue);
//									} else if (attributeName.equals("sn")) {
//										employee.setEmployeeLastName(attributeValue);
//									} else if (attributeName.equals("employeeID")) {
//										long tempId = extractNumericValue(attributeValue);
//										employee.setEmployeeId(tempId);
//
//									} else if (attributeName.equals("title")) {
//
//										employee.setDesignation(attributeValue);
//
//									} else if (attributeName.equals("sAMAccountName")) {
//
//										employee.setMail(attributeValue + "@stgit.com");
//
//									}
//
//									data.put(attributeName, attributeValue);
//
//								} catch (NamingException e) {
//									// Handle any exceptions if necessary
//								}
//							}
//						}
//
//						// Add the data map to the list
//
//						allEmployee.add(data);
//
//						NamingEnumeration<?> valueEnum = attrs.getAll();
//						while (valueEnum.hasMore()) {
//							String attributeValue = valueEnum.next().toString();
////					System.err.println(attributeValue);
//
//						}
//
////			----------------------------------------------------------------------------
////			List<Map<String, Object>> allEmployee = new ArrayList<Map<String, Object>>();
////
////			while (answer.hasMore()) {
////
////				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
////
//////				System.out.println(attrs);
////
////				Map<String, Object> data = new HashMap<String, Object>();
////
////				data.put("givenname", attrs.get("givenname"));
////
////				data.put("sn", attrs.get("sn"));
////
////				data.put("mail", attrs.get("mail"));
////
////				data.put("employeeID", attrs.get("employeeID"));
////
////				data.put("title", attrs.get("title"));
////				data.put("Mobile", attrs.get("department"));
////				 NamingEnumeration<?> valueEnum = attrs.getAll();
////				 while (valueEnum.hasMore()) {
////					
////			            String attributeValue = valueEnum.next().toString();
////			            System.err.println(attributeValue);
////			        }
////				
//
//						allEmployee.add(data);
//						// System.out.println(data);
//
////				return data;
//						allEmployees.add(employee);
//					}
//				}
//
//			}
////			System.err.println(allEmployee);
////			System.err.println(allEmployees);
//
//			return allEmployees;
//
////			System.out.println("user not found.");
//
//		} catch (Exception var6) {
//
//			var6.printStackTrace();
//
//		}
//
//		return null;
//
//	}
//
//	private static long extractNumericValue(String employeeID) {
//
//		// Split the string by the hyphen
//
//		String[] parts = employeeID.split("-");
//
//		// Ensure that there is a part after the hyphen
//
//		if (parts.length > 1) {
//
//			try {
//
//				// Parse the numeric part as a long
//
//				return Long.parseLong(parts[1]);
//
//			} catch (NumberFormatException e) {
//
//				// Handle the case where the numeric part is not a valid long
//
//				e.printStackTrace();
//
//			}
//
//		}
//
//		// Return a default value or handle the case where extraction fails
//
//		return -1;
//
//	}
//
//	private static SearchControls getSearchControls() {
//
//		SearchControls cons = new SearchControls();
//
//		cons.setSearchScope(2);
//
//		String[] attrIDs = new String[] { "employeeID", "distinguishedName", "sn", "givenname", "mail", "title",
//
//				"sAMAccountName" };
//
//		cons.setReturningAttributes(attrIDs);
//
//		return cons;
//
//	}
//
//	@SuppressWarnings("unused")
//
//	private static void savePhoto(String userName, byte[] photo) throws IOException {
//
//		FileOutputStream os = new FileOutputStream("d:/userPhotos" + userName + ".jpg");
//
//		os.write(photo);
//
//		os.flush();
//
//		os.close();
//
//	}
//
//}
