import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vehicle {
	private String VIN, entryDate, owner;
	private static final Pattern threeComma = Pattern.compile("(^[^,]*,[^,]*,[^,]*)");
	private static final Pattern dataPattern = Pattern.compile("(^[^,]+)?,([^,]*)?,([^,]+)?$");

	Vehicle(String data) {
		data = simplify(data);
		String[] datum = data.split(",");
		if (datum[0].equals("VIN NUMBER")) return;
		if (datum.length == 3) {
			VIN = datum[0].toUpperCase().trim();
			entryDate = datum[1].trim();
			owner = datum[2].toUpperCase().trim();
		} else {
			//The formatting of the file is broken, but we can't risk losing any data
			Matcher m = dataPattern.matcher(data);
			String vi = "???";
			String en = "???";
			String ow = "???";
			if (m.find()) {
				vi = m.group(1);
				en = m.group(2);
				ow = m.group(3);
				vi = fixEmpty(vi);
				en = fixEmpty(en);
				ow = fixEmpty(ow);
			}
			VIN = vi.toUpperCase().trim();
			entryDate = en.trim();
			owner = ow.toUpperCase().trim();
		}
	}

	private static String fixEmpty(String s) {
		return s == null || s.isEmpty() ? "???" : s;
	}

	@Override
	public String toString() {
		return String.format("%-17s,\t%-10s,\t%s", VIN, entryDate, owner);
	}

	@Override
	public int hashCode() {
		return this.VIN.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vehicle) {
			Vehicle v = (Vehicle) obj;
			if (VIN.equals(v.VIN) &&
					equalsIgnoreUnknown(entryDate, v.entryDate) &&
					equalsIgnoreUnknown(owner, v.owner)) {
				//Merge wildcard data
				if (!entryDate.equals("???") ^ !v.entryDate.equals("???")) {
					entryDate = entryDate.equals("???") ? v.entryDate : entryDate;
					v.entryDate = v.entryDate.equals("???") ? entryDate : v.entryDate;
				}
				if (!owner.equals("???") ^ !v.owner.equals("???")) {
					owner = owner.equals("???") ? v.owner : owner;
					v.owner = v.owner.equals("???") ? owner : v.owner;
				}
				return true;
			}
		}
		return false;
	}

	private static boolean equalsIgnoreUnknown(String s1, String s2) {
		//They are the same if either one is a wildcard
		return s1.equals("???") || s2.equals("???") || s1.equals(s2);
	}

	private static String simplify(String s) {
		s = s.toUpperCase()
				.replace("\"", "")
				.replaceAll("\t", "")
				.trim();
		Matcher m = threeComma.matcher(s);
		if (m.find()) {
			s = m.group(1);
		}
		return s;
	}

	public String getVIN() {
		return VIN;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public String getOwner() {
		return owner;
	}
}
