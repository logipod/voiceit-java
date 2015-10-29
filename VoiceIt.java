import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VoiceIt {

	String developerId;

	public VoiceIt(String developerId) {
		this.developerId = developerId;
	}

	private String GetSHA256(String data) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			sha.update(data.getBytes());
			byte[] hash = sha.digest();

			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

	private String readInputStream(InputStream inputStream) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			int result = inputStream.read();
			while (result != -1) {
				outputStream.write((byte) result);
				result = inputStream.read();
			}
			return outputStream.toString();
		}
	}

	public String getUser(String email, String password) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/users");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("GET");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String createUser(String email, String password,String firstName,String lastName, String phone1,String phone2,String phone3) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/users");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.addRequestProperty("VsitFirstName", firstName);
			connection.addRequestProperty("VsitLastName", lastName);
			connection.addRequestProperty("VsitPhone1", phone1);
			connection.addRequestProperty("VsitPhone2", phone2);
			connection.addRequestProperty("VsitPhone3", phone3);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	//Added CreateUser Method Calls to make the phone numbers optional
	public String createUser(String email, String password,String firstName,String lastName) throws IOException
	{
		return createUser(email,password,firstName,lastName,"","","");
	}

	public String createUser(String email, String password,String firstName,String lastName,String phone1) throws IOException
	{
		return createUser(email,password,firstName,lastName,phone1,"","");
	}

	public String createUser(String email, String password,String firstName,String lastName,String phone1,String phone2) throws IOException
	{
		return createUser(email,password,firstName,lastName,phone1,phone2,"");
	}


	public String setUser(String email, String password,String firstName,String lastName, String phone1,String phone2,String phone3) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/users");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("PUT");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.addRequestProperty("VsitFirstName", firstName);
			connection.addRequestProperty("VsitLastName", lastName);
			connection.addRequestProperty("VsitPhone1", phone1);
			connection.addRequestProperty("VsitPhone2", phone2);
			connection.addRequestProperty("VsitPhone3", phone3);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String setUser(String email, String password,String firstName,String lastName) throws IOException
	{
		return setUser(email,password,firstName,lastName,"","","");
	}

	public String setUser(String email, String password,String firstName,String lastName,String phone1) throws IOException
	{
		return setUser(email,password,firstName,lastName,phone1,"","");
	}

	public String setUser(String email, String password,String firstName,String lastName,String phone1,String phone2) throws IOException
	{
		return setUser(email,password,firstName,lastName,phone1,phone2,"");
	}

	public String deleteUser(String email, String password) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/users");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("DELETE");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String createEnrollment(String email, String password,String pathToEnrollmentWav) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/enrollments");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		byte [] myData = Files.readAllBytes(Paths.get(pathToEnrollmentWav));
		try {
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.setRequestProperty("Content-Type","audio/wav");
			DataOutputStream request = new DataOutputStream(connection.getOutputStream());
			request.write(myData);
			request.flush();
			request.close();
			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String createEnrollmentByWavURL(String email, String password,String urlToEnrollmentWav) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/enrollments/bywavurl");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.addRequestProperty("VsitwavURL", urlToEnrollmentWav);
			connection.setRequestProperty("Content-Type","audio/wav");

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String deleteEnrollment(String email, String password,String enrollmentId) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/enrollments"+"/"+enrollmentId);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("DELETE");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String getEnrollments(String email, String password) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/enrollments");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("GET");
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String authentication(String email, String password,String pathToAuthenticationWav,String accuracy, String accuracyPasses, String accuracyPassIncrement, String confidence) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/authentications");
		byte [] myData = Files.readAllBytes(Paths.get(pathToAuthenticationWav));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.addRequestProperty("VsitAccuracy", accuracy);
			connection.addRequestProperty("VsitAccuracyPasses", accuracyPasses);
			connection.addRequestProperty("VsitAccuracyPassIncrement", accuracyPassIncrement);
			connection.addRequestProperty("VsitConfidence", confidence);
			connection.setRequestProperty("Content-Type","audio/wav");

			DataOutputStream request = new DataOutputStream(connection.getOutputStream());
			request.write(myData);
			request.flush();
			request.close();

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public String authenticationByWavURL(String email, String password,String urlToAuthenticationWav,String accuracy, String accuracyPasses,String accuracyPassIncrement,String confidence) throws IOException {
		URL url = new URL("https://siv.voiceprintportal.com/sivservice/api/authentications/bywavurl");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.addRequestProperty("VsitEmail", email);
			connection.addRequestProperty("VsitPassword", GetSHA256(password));
			connection.addRequestProperty("VsitDeveloperId", developerId);
			connection.addRequestProperty("VsitAccuracy", accuracy);
			connection.addRequestProperty("VsitAccuracyPasses", accuracyPasses);
			connection.addRequestProperty("VsitAccuracyPassIncrement", accuracyPassIncrement);
			connection.addRequestProperty("VsitConfidence", confidence);
			connection.addRequestProperty("VsitwavURL", urlToAuthenticationWav);
			connection.setRequestProperty("Content-Type","audio/wav");

			// read response
			try (InputStream inputStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream()) {
				return readInputStream(inputStream);
			}
			}
			finally {
			if (connection != null)
				connection.disconnect();
		}
	}


}
