import java.io.IOException;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
[...]

public class Txt2wav {

	static {
	[...]
	}

	public static void main(String argv[]) {
		[...]
		int chan_handle, res;
		String voice_name;
		String license_name;
		String text_name;
		String ssml_name;
		String out_file;
		String text_out = new String();
		String ssml_header = new String();
		String ssml_rate = new String();
		String ssml_ratec = new String();
		String ssml_sbreak = new String();
		String ssml_pbreak = new String();
		String result = new String();
		BufferedWriter ssml_script = null;
		TtsEngineCallback cb;
		byte[] utf8bytes;
		// By default use the simplified API. Set this to false to use the
		// standard API.
		boolean simplified = true;

		// cerevoice_isabella_3.2.0_48k
		// cerevoice_isabella_hmm_4.0.1_48k
		voice_name = "/home/jordi/cereproc/voices/cerevoice_isabella_3.2.0_48k.voice";
		license_name = "/home/jordi/cereproc/license.lic";
		text_name = "/home/jordi/cereproc/tags SSML/Automatization/news_01.txt";// Name of the input text file.
		ssml_name = "/home/jordi/cereproc/tags SSML/Automatization/news_01.ssml";// Name of the SSML file that will be created.
		out_file = "/home/jordi/cereproc/tags SSML/Automatization/news_01_unit.wav";// Name of the output audio file.
		ssml_header = "<?xml version=\"1.0\"?>\n<speak version=\"1.1\" xmlns=\"http://www.w3.org/2001/10/synthesis\"\nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\nxsi:schemaLocation=\"http://www.w3.org/2001/10/synthesis\n          http://www.w3.org/TR/speech-synthesis11/synthesis.xsd\"\nxml:lang=\"de-DE\">\n";
		ssml_rate = "<prosody rate='-10%'>\n";//rate applied in the whole text.
		ssml_ratec = "</prosody>";
		ssml_sbreak = "<break time='600ms'/>";
		ssml_pbreak = "<break time='2s'/>";

		// Load the voice using the simple API
		[...]
		// Load the text and do synthesis using the simplified API
		if (simplified) {
			try {
				File file = new File(ssml_name);
				// Open the text file
				StringBuilder text = new StringBuilder();
				FileReader f = new FileReader(text_name);
				BufferedReader br = new BufferedReader(f);
				String strLine;
				// Read file line-by-line, add it to the buffer.
				// readLine strips line break so reattach it
				while ((strLine = br.readLine()) != null) {
					text.append(strLine + '\n');
				}
				f.close();

				Pattern p = Pattern.compile("\\. ");// Phrase dots detection
				Matcher m = p.matcher(text);
				StringBuffer sb = new StringBuffer();
				while ((m.find())) {
					m.appendReplacement(sb, ssml_sbreak);
				}
				m.appendTail(sb);
				text_out = sb.toString();

				Pattern p2 = Pattern.compile("\\\\n\\\\n");// End of paragraph detection
				Matcher m2 = p2.matcher(text_out);
				StringBuffer sb_out = new StringBuffer();
				while ((m2.find())) {
					m2.appendReplacement(sb_out, ssml_pbreak);
				}
				m2.appendTail(sb_out);
				result = ssml_header + ssml_rate + sb_out.toString() + ssml_ratec;
				System.out.println(result);
				ssml_script = new BufferedWriter(new FileWriter(file));
				ssml_script.write(result);// output SSML file
				[...]
			} catch (IOException e) {
				System.err.println("ERROR: " + e.getMessage());
			} finally {
				if (ssml_script != null) {
					try {
						ssml_script.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		[...]

}
