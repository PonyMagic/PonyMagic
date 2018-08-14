package net.braunly.ponymagic.exp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

public class ExperienceStorage {
	@Getter
	@Nonnull
	private final ImmutableMap<String, Double> mine = new ImmutableMap.Builder<String, Double>()
			.putAll(loadExperienceTable("mine")).build();
	@Getter
	@Nonnull
	private final ImmutableMap<String, Double> place = new ImmutableMap.Builder<String, Double>()
			.putAll(loadExperienceTable("place")).build();
	@Getter
	@Nonnull
	private final ImmutableMap<String, Double> craft = new ImmutableMap.Builder<String, Double>()
			.putAll(loadExperienceTable("craft")).build();
	@Getter
	@Nonnull
	private final ImmutableMap<String, Double> kill = new ImmutableMap.Builder<String, Double>()
			.putAll(loadExperienceTable("kill")).build();

	private ExperienceStorage() {
	}

	private static class ExperienceStorageHolder {
		static final ExperienceStorage INSTANCE = new ExperienceStorage();
	}

	public static ExperienceStorage getInstance() {
		return ExperienceStorageHolder.INSTANCE;
	}

	private static HashMap<String, Double> loadExperienceTable(@Nonnull String name) {
		HashMap<String, Double> result = new HashMap<>();
		try {
			// "https://docs.google.com/spreadsheets/d/1cnrFHQHQ2xgITkFdIohpT8v3tziTJb_JaUwhZTs5s4U/gviz/tq?tqx=out:csv&sheet="
			URL expTableUrl = new URL(
					"https://docs.google.com/spreadsheets/d/1Q9AOXNqzMnlobUEnAQ4J-dbRal2WhIQ1tR0PrBXLIlw/gviz/tq?tqx=out:csv&sheet="
							+ name);
			BufferedReader br = new BufferedReader(new InputStreamReader(expTableUrl.openStream()));
			String line;
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.replace("\"", "").split(",");
				result.put(lineArray[0], Double.parseDouble(lineArray[1]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}