package de.joshi1999.pluginhelper.utils;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

import java.util.Set;
import org.reflections.Reflections;

public class ClassAnalyzer {
  public static Set<Class<?>> findClassesByAnnotation(String packageName, Class<?> annotation) {
    Reflections r = new Reflections(packageName);
    return r.get(SubTypes.of(TypesAnnotated.with(annotation)).asClass());
  }
}
