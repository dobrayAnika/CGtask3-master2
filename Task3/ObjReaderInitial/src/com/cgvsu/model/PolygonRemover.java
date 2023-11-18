package com.cgvsu.model;
import java.util.ArrayList;
import java.util.Iterator;

public class PolygonRemover {

    public static void removePolygons(Model model, ArrayList<Integer> polygonIndices, boolean removeFreeVertices) {
        // Step 1: Remove selected polygons
        removeSelectedPolygons(model, polygonIndices);

        // Step 2: Remove free vertices if specified
        if (removeFreeVertices) {
            removeFreeVertices(model);
        }
    }

    private static void removeSelectedPolygons(Model model, ArrayList<Integer> polygonIndices) {
        Iterator<Polygon> iterator = model.polygons.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            iterator.next();
            if (polygonIndices.contains(currentIndex)) {
                iterator.remove();
            }
            currentIndex++;
        }
    }


    private static void removeFreeVertices(Model model) {
        ArrayList<Integer> verticesToRemove = new ArrayList<>();

        // Step 3: Check and mark free vertices
        for (int i = 0; i < model.vertices.size(); i++) {
            if (!isVertexUsed(model, i)) {
                verticesToRemove.add(i);
            }
        }

        // Step 4: Remove free vertices
        removeVertices(model, verticesToRemove);
    }

    private static boolean isVertexUsed(Model model, int vertexIndex) {
        for (Polygon polygon : model.polygons) {
            if (polygon.getVertexIndices().contains(vertexIndex)) {
                return true;
            }
        }
        return false;
    }

    private static void removeVertices(Model model, ArrayList<Integer> verticesToRemove) {
        // Step 5: Remove vertices and update polygon indices
        for (int i = verticesToRemove.size() - 1; i >= 0; i--) {
            int vertexIndexToRemove = verticesToRemove.get(i);
            model.vertices.remove(vertexIndexToRemove);

            // Update polygon indices
            for (Polygon polygon : model.polygons) {
                updateIndices(polygon.getVertexIndices(), vertexIndexToRemove);
                updateIndices(polygon.getTextureVertexIndices(), vertexIndexToRemove);
                updateIndices(polygon.getNormalIndices(), vertexIndexToRemove);
            }
        }
    }

    private static void updateIndices(ArrayList<Integer> indices, int vertexIndexToRemove) {
        for (int i = indices.size() - 1; i >= 0; i--) {
            int currentIndex = indices.get(i);
            if (currentIndex == vertexIndexToRemove) {
                indices.remove(i);
            } else if (currentIndex > vertexIndexToRemove) {
                indices.set(i, currentIndex - 1);
            }
        }
    }
}