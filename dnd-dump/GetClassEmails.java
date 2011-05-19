import java.io.*;
import java.net.*;
import java.util.*;

public class GetClassEmails {
	static boolean done = false;
	public static void main(String args[]) {
		String year;
		if(args.length == 0) {
			year = Integer.toString((new GregorianCalendar()).get(Calendar.YEAR)+4).substring(2);
			// System.out.println("No year specified using students graduating in 4 years: the " + year + "'s");
		}
		else
			year = args[0]; // Argument should be 2 character year
		
		Set<String> names = new HashSet<String>(2000);
		try {
			Socket socket = new Socket("dnd.dartmouth.edu", 902);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			in.readLine();
			String cur = "a";
			String response;
			while(!done) {
				// System.out.println(cur);
				String dndQuery = "LOOKUP " + cur + " " + year + ",email";
				out.println(dndQuery);
				out.flush();
				response = in.readLine();
				// No matches found
				if(response.charAt(0) != '1') {
					cur = increment(cur);
					continue;
				}
				int numResp = Integer.parseInt(response.substring(response.indexOf(' ')+1, response.lastIndexOf(' ')));
				
				for(int i = 0; i < numResp; i++) {
					response = in.readLine();
					String name = response.substring(4);
					names.add(name);
				}
				response = in.readLine();
				// Too many matches found
				if(response.substring(0,3).equals("201")) {
					cur = cur + "a";
				}
				// Between 1 and 25 matches found
				else {
					cur = increment(cur);
				}
				
			}
		} catch(IOException e) {
			System.err.println("Error");
			e.printStackTrace();
		}
		for(String name : names)
			System.out.println(name);
		// System.out.println("Total: " + names.size());
	}
	
	private static String increment(String cur) {
		if(cur.length() == 0) {
			done = true;
			return cur;
		}
		if(cur.charAt(cur.length()-1) == 'z')
			return increment(cur.substring(0, cur.length()-1));
		
		return cur.substring(0, cur.length()-1) + ((char)(cur.charAt(cur.length()-1)+1));
	}
}
