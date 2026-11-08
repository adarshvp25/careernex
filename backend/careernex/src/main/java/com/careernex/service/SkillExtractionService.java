
package com.careernex.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class SkillExtractionService {

    private final Map<String, List<String>> skillCatalogue =
            new LinkedHashMap<>();

    public SkillExtractionService() {

        skillCatalogue.put("HTML5", List.of("HTML5", "HTML"));

        skillCatalogue.put("CSS3", List.of("CSS3", "CSS"));

        skillCatalogue.put("JavaScript",
                List.of("JavaScript", "ECMAScript"));

        skillCatalogue.put("Bootstrap", List.of("Bootstrap"));

        skillCatalogue.put("React", List.of("React", "React.js"));

        skillCatalogue.put("Node.js", List.of("Node.js"));

        skillCatalogue.put("Express.js", List.of("Express.js"));

        skillCatalogue.put("MongoDB", List.of("MongoDB"));

        skillCatalogue.put("MySQL", List.of("MySQL"));

        skillCatalogue.put("Java", List.of("Java"));

        skillCatalogue.put("Python", List.of("Python"));

        skillCatalogue.put("C", List.of("C"));

        skillCatalogue.put("C++", List.of("C++"));

        skillCatalogue.put("DOM Manipulation",
                List.of("DOM Manipulation"));

        skillCatalogue.put("Fetch API", List.of("Fetch API"));

        skillCatalogue.put("Asynchronous JavaScript",
                List.of("Asynchronous JavaScript", "Async JavaScript"));

        skillCatalogue.put("Git", List.of("Git"));

        skillCatalogue.put("GitHub", List.of("GitHub"));

        skillCatalogue.put("VS Code", List.of("VS Code", "Visual Studio Code"));

        skillCatalogue.put("Responsive Design",
                List.of("Responsive Design", "Responsive Web Design"));

        skillCatalogue.put("Browser DevTools",
                List.of("Browser DevTools", "Chrome DevTools"));
    }

    public List<String> extractSkills(String resumeText) {

        List<String> detectedSkills = new ArrayList<>();

        if (resumeText == null || resumeText.isBlank()) {
            return detectedSkills;
        }

        String normalizedText = resumeText.toLowerCase();

        for (Map.Entry<String, List<String>> entry
                : skillCatalogue.entrySet()) {

            String skillName = entry.getKey();

            for (String alias : entry.getValue()) {

                String regex = "(?<![a-z0-9])"
                        + Pattern.quote(alias.toLowerCase())
                        + "(?![a-z0-9])";

                if (Pattern.compile(regex).matcher(normalizedText).find()) {

                    detectedSkills.add(skillName);
                    break;
                }
            }
        }

        return detectedSkills;
    }
}
