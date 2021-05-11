package net.braunly.ponymagic.util;

import com.google.common.io.Files;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class NBTJsonUtil {
	public static String convert(NBTTagCompound compound) {
		List<JsonLine> list = new ArrayList<>();
		JsonLine line = readTag("", compound, list);
		line.removeComma();
		return convertList(list);
	}

	public static NBTTagCompound convert(String json) throws JsonException {
		json = json.trim();
		JsonFile file = new JsonFile(json);
		if (!json.startsWith("{") || !json.endsWith("}"))
			throw new JsonException("Not properly incapsulated between { }", file);

		NBTTagCompound compound = new NBTTagCompound();
		fillCompound(compound, file);
		return compound;
	}

	public static void fillCompound(NBTTagCompound compound, JsonFile json) throws JsonException {
		if (json.startsWith("{") || json.startsWith(","))
			json.cut(1);
		if (json.startsWith("}"))
			return;
		int index = json.indexOf(":");
		if (index < 1)
			throw new JsonException("Expected key after ,", json);

		String key = json.substring(0, index);
		json.cut(index + 1);

		NBTBase base = readValue(json);

		if (base == null)
			base = new NBTTagString();

		if (key.startsWith("\""))
			key = key.substring(1);
		if (key.endsWith("\""))
			key = key.substring(0, key.length() - 1);

		compound.setTag(key, base);
		if (json.startsWith(","))
			fillCompound(compound, json);

	}

	public static NBTBase readValue(JsonFile json) throws JsonException {
		if (json.startsWith("{")) {
			NBTTagCompound compound = new NBTTagCompound();
			fillCompound(compound, json);
			if (!json.startsWith("}")) {
				throw new JsonException("Expected }", json);
			}
			json.cut(1);

			return compound;
		}
		if (json.startsWith("[")) {
			json.cut(1);
			NBTTagList list = new NBTTagList();

			NBTBase value = readValue(json);
			while (value != null) {
				list.appendTag(value);
				if (!json.startsWith(","))
					break;
				json.cut(1);
				value = readValue(json);
			}
			if (!json.startsWith("]")) {
				throw new JsonException("Expected ]", json);
			}
			json.cut(1);

			if (list.getTagType() == 3) {
				int[] arr = new int[list.tagCount()];
				for (int i = 0; list.tagCount() > 0; i++) {
					arr[i] = ((NBTTagInt) list.removeTag(0)).getInt();
				}
				return new NBTTagIntArray(arr);
			}
			if (list.getTagType() == 1) {
				byte[] arr = new byte[list.tagCount()];
				for (int i = 0; list.tagCount() > 0; i++) {
					arr[i] = ((NBTTagByte) list.removeTag(0)).getByte();
				}
				return new NBTTagByteArray(arr);
			}

			return list;
		}
		if (json.startsWith("\"")) {
			json.cut(1);
			StringBuilder s = new StringBuilder();
			boolean ignore = false;
			while (!json.startsWith("\"") || ignore) {
				String cut = json.cutDirty(1);
				ignore = cut.equals("\\");
				s.append(cut);
			}
			json.cut(1);
			return new NBTTagString(s.toString().replace("\\\"", "\""));
		}
		StringBuilder s = new StringBuilder();
		while (!json.startsWith(",", "]", "}")) {
			s.append(json.cut(1));
		}
		s = new StringBuilder(s.toString().trim().toLowerCase());
		if (s.length() == 0)
			return null;
		try {
			if (s.toString().endsWith("d")) {
				return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
			}
			if (s.toString().endsWith("f")) {
				return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
			}
			if (s.toString().endsWith("b")) {
				return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
			}
			if (s.toString().endsWith("s")) {
				return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
			}
			if (s.toString().endsWith("l")) {
				return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
			}
			if (s.toString().contains("."))
				return new NBTTagDouble(Double.parseDouble(s.toString()));
			else
				return new NBTTagInt(Integer.parseInt(s.toString()));
		} catch (NumberFormatException ex) {
			throw new JsonException("Unable to convert: " + s + " to a number", json);
		}
	}

	private static List<NBTBase> getListData(NBTTagList list) {
		return ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, list, 1);
	}

	private static JsonLine readTag(String name, NBTBase base, List<JsonLine> list) {
		if (!name.isEmpty())
			name = "\"" + name + "\": ";
		if (base.getId() == 8) {// NBTTagString
			String data = ((NBTTagString) base).getString();
			data = data.replace("\"", "\\\""); // replace " with \"
			list.add(new JsonLine(name + "\"" + data + "\""));
		} else if (base.getId() == 9) {// NBTTagList
			list.add(new JsonLine(name + "["));
			NBTTagList tags = (NBTTagList) base;
			JsonLine line = null;
			List<NBTBase> data = getListData(tags);
			for (NBTBase b : data)
				line = readTag("", b, list);
			if (line != null)
				line.removeComma();
			list.add(new JsonLine("]"));
		} else if (base.getId() == 10) { // NBTTagCompound
			list.add(new JsonLine(name + "{"));
			NBTTagCompound compound = (NBTTagCompound) base;
			JsonLine line = null;
			for (Object key : compound.getKeySet())
				line = readTag(key.toString(), compound.getTag(key.toString()), list);

			if (line != null)
				line.removeComma();

			list.add(new JsonLine("}"));
		} else if (base.getId() == 11) {// NBTTagList
			list.add(new JsonLine(name + base.toString().replaceFirst(",]", "]")));
		} else {
			list.add(new JsonLine(name + base));
		}
		JsonLine line = list.get(list.size() - 1);
		line.line += ",";
		return line;
	}

	private static String convertList(List<JsonLine> list) {
		StringBuilder json = new StringBuilder();
		int tab = 0;
		for (JsonLine tag : list) {
			if (tag.reduceTab())
				tab--;
			for (int i = 0; i < tab; i++) {
				json.append("    ");
			}
			json.append(tag).append("\n");

			if (tag.increaseTab())
				tab++;
		}
		return json.toString();
	}

	static class JsonLine {
		private String line;

		public JsonLine(String line) {
			this.line = line;
		}

		public void removeComma() {
			if (line.endsWith(","))
				line = line.substring(0, line.length() - 1);
		}

		public boolean reduceTab() {
			int length = line.length();
			return length == 1 && (line.endsWith("}") || line.endsWith("]"))
					|| length == 2 && (line.endsWith("},") || line.endsWith("],"));
		}

		public boolean increaseTab() {
			return line.endsWith("{") || line.endsWith("[");
		}

		@Override
		public String toString() {
			return line;
		}
	}

	static class JsonFile {
		private String original;
		private String text;

		public JsonFile(String text) {
			this.text = text;
			this.original = text;
		}

		public String cutDirty(int i) {
			String s = text.substring(0, i);
			text = text.substring(i);
			return s;
		}

		public String cut(int i) {
			String s = text.substring(0, i);
			text = text.substring(i).trim();
			return s;
		}

		public String substring(int beginIndex, int endIndex) {
			return text.substring(beginIndex, endIndex);
		}

		public int indexOf(String s) {
			return text.indexOf(s);
		}

		public String getCurrentPos() {
			int lengthOr = original.length();
			int lengthCur = text.length();
			int currentPos = lengthOr - lengthCur;
			String done = original.substring(0, currentPos);
			String[] lines = done.split("\r\n|\r|\n");

			int pos = 0;
			String line = "";
			if (lines.length > 0) {
				pos = lines[lines.length - 1].length();
				line = original.split("\r\n|\r|\n")[lines.length - 1].trim();
			}

			return "Line: " + lines.length + ", Pos: " + pos + ", Text: " + line;
		}

		public boolean startsWith(String... ss) {
			for (String s : ss)
				if (text.startsWith(s))
					return true;
			return false;
		}

		public boolean endsWith(String s) {
			return text.endsWith(s);
		}
	}

	public static NBTTagCompound loadFile(File file) throws IOException, JsonException {
		return convert(Files.toString(file, Charsets.UTF_8));
	}

	public static void saveFile(File file, NBTTagCompound compound) throws IOException {
		String json = convert(compound);
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8)) {
			writer.write(json);
		}
	}
}
