import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
    private CharMatrix charMatrix;

    public static void main(String[] args) throws IOException {
        new Day10().start();
    }

    private void start() throws IOException {
        charMatrix = new CharMatrix(InputUtil.readAsLines("input10.txt"), '.');
        CharMatrix.Position startPosition = charMatrix.find('S').orElseThrow();
        startPosition.set('|');
        List<CharMatrix.Position> loop = calcLoop(startPosition);
        int first = loop.size() / 2;
        System.out.println(first);
        CharMatrix zoom = calcZoom(loop);
        floodfill(zoom.new Position(0, 0));
        int second = 0;
        for (int y = 1; y < zoom.getHeight(); y += 3) {
            for (int x = 1; x < zoom.getWidth(); x += 3) {
                if (zoom.get(x, y) == '.') {
                    second++;
                }
            }
        }
        System.out.println(second);
    }

    private CharMatrix calcZoom(List<CharMatrix.Position> loop)
    {
        CharMatrix result = new CharMatrix(charMatrix.getHeight() * 3, charMatrix.getHeight() * 3);
        for (CharMatrix.Position position : loop) {
            CharMatrix.Position zoomPosition = result.new Position(position.getX() * 3 + 1, position.getY() * 3 + 1);
            getNeighbours(position)
                    .stream()
                    .map(n -> result.new Position(n.getX() - position.getX(),
                            n.getY() - position.getY()).add(zoomPosition))
                    .forEach(n -> n.set('*'));
            zoomPosition.set('*');
        }
        return result;
    }

    private void floodfill(CharMatrix.Position position) {
        Deque<CharMatrix.Position> todo = new ArrayDeque<>();
        position.set('O');
        todo.add(position);
        while (!todo.isEmpty()) {
            CharMatrix.Position current = todo.remove();
            current.set('O');
            current.getNeighbours().stream()
                    .filter(n -> n.get() == '.')
                    .forEach(n -> {
                            todo.add(n);
                            n.set('O');
                    });
        }
    }

    private List<CharMatrix.Position> calcLoop(CharMatrix.Position start)
    {
        List<CharMatrix.Position> result = new ArrayList<>();
        CharMatrix.Position previous = start;
        result.add(previous);
        CharMatrix.Position current = getNeighbours(previous).get(0);
        while (!current.equals(start)) {
            List<CharMatrix.Position> neigh = getNeighbours(current);
            if (!neigh.contains(previous)) {
                throw new IllegalStateException();
            }
            neigh.remove(previous);
            CharMatrix.Position next = neigh.get(0);
            result.add(current);
            previous = current;
            current = next;
        }
        return result;
    }

    private List<CharMatrix.Position> getNeighbours(CharMatrix.Position position)
    {
        List<CharMatrix.Position> result = List.of();
        char c = position.get();
        switch (c) {
            case '|':
                result = List.of(position.add(0, -1), position.add(0, 1));
                break;
            case '-':
                result = List.of(position.add(-1, 0), position.add(1, 0));
                break;
            case 'L':
                result = List.of(position.add(0, -1), position.add(1, 0));
                break;
            case 'J':
                result = List.of(position.add(-1, 0), position.add(0, -1));
                break;
            case '7':
                result = List.of(position.add(-1, 0), position.add(0, 1));
                break;
            case 'F':
                result = List.of(position.add(1, 0), position.add(0, 1));
                break;
        }
        return result.stream().filter(CharMatrix.Position::isValid).collect(Collectors.toList());
    }
}
