public enum Direction {
    North, South, West, East;

    static public boolean equals(Direction d1, Direction d2) {
        if (d1 == d2) {
            return true;
        }
        return false;
    }


   static public Direction straightDirection(Direction d) {
        if (d == North) {
            return North;
        } else if (d == East) {
            return East;
        } else if (d == South) {
            return South;
        } else if (d == West) {
            return West;
        }

        return null;
    }


   static public Direction rightDirection(Direction d) {
        if (d == North) {
            return East;
        } else if (d == East) {
            return South;
        } else if (d == South) {
            return West;
        } else if (d == West) {
            return North;
        }

        return null;
    }


   static public Direction leftDirection(Direction d) {
        if (d == North) {
            return West;
        } else if (d == East) {
            return North;
        } else if (d == South) {
            return East;
        } else if (d == West) {
            return South;
        }

        return null;
    }

}
