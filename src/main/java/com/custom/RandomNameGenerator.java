package com.custom;

import java.util.Random;

public class RandomNameGenerator  {

    private static final String[] VOWELS = {"a", "e", "i", "o", "u"};
    private static final String[] CONSONANTS = {"b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "y", "z"};
    private static final String[] MIDDLE_NAMES = {"Anne", "James", "Elizabeth", "John", "Marie", "William", "Emily", "David", "Sophia", "Michael", "Sarah", "Daniel", "Emma", "Matthew","Mary","Patricia","Linda","Elizabeth","Barbara","Jennifer","Maria","Susan","Dorothy","Lisa","Margaret","Nancy","Karen","Betty","Helen","Donna","Sandra","Carol","Ruth","Sharon","Michelle","Laura","Kimberly","Sarah","Deborah","Jessica","Shirley","Cynthia","Angela","Melissa","Brenda","Amy","Rebecca","Virginia","Anna","Kathleen","Pamela","Martha","Debra","Amanda","Carolyn","Stephanic","Christinc","Catherine","Marie","Janet","Frances","Ann","Julic","Alicc","Joycc","Dianc","Teresa","Doris","Heather","Gloria","Evelyn","Chcryl","Jcan","Mildrcd","Katherinc","Joan","Ashley","Judith","Rose","Anice","Kelly","Nicolc","Judy","Christina","Kathy","Theresa","Beverly","Denise","Tammy","Irene","Jane","Lori","Rachel","Marilyn","Kathryn","Sara","Andrea","Louise","Wanda","Anne","Jacqueline","Bonnie","Ruby","Lois","Tina","Julia","Phyllis","Norma","Lillian","Paula","Diana","Annie","Emily","Crystal","Robin","Peggy","Gladys","Rita","Dawn","Connie","Florence","Tracy","Edna","Tiffany","Carmen","Rosa","Cindy","Wendy","Victoria","Edith","Grace","Sylvia","Kim","Sherry","Thelma","Josephine","Sheila", "Ethel", "Ellen", "Shannon", "Elaine", "Carrie", "Charlotte", "Marjorie", "Monica", "Esther",
            "Pauline", "Emma", "Juanita", "Anita", "Rhonda", "Hazel", "Amber", "Eva", "Debbie", "April", "Leslie",
            "Clara", "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie", "Alicia", "Suzanne", "Danielle", "Megan",
            "Michele", "Gail", "Bertha", "Veronica", "Darlene", "Jill", "Erin", "Lauren", "Geraldine", "Joann",
            "Cathy", "Lorraine", "Lynn", "Sally", "Erica", "Beatrice", "Dolores", "Regina", "Bernice", "Audrey",
            "Yvonne", "Annette", "June", "Dana", "Stacy", "Marion", "Samantha", "Ana", "Renee", "Ida", "Vivian",
            "Roberta", "Holly", "Melanie", "Loretta", "Brittany", "Yolanda", "Laurie", "Kristen", "Katie",
            "Jeanette", "Alma", "Elsie", "Vanessa", "Sue", "Beth", "Jeanne"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White","James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas",
            "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
            "Ronald", "Anthony", "Kevin", "Gary", "Timothy", "Matthew", "Jason", "Larry", "Jeffrey", "Frank",
            "Scott", "Eric", "Jose", "Stephen", "Raymond", "Andrew", "Gregory", "Joshua", "Jerry", "Dennis",
            "Walter", "Peter", "Harold", "Douglas", "Patrick", "Henry", "Carl", "Arthur", "Ryan", "Roger", "Joe","Juan", "Jack", "Albert", "Jonathan", "Terry", "Gerald", "Keith", "Samuel", "Justin", "Willie",
            "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam", "Harry", "Wayne",
            "Billy", "Steve", "Fred", "Aaron", "Louis", "Jeremy", "Randy", "Howard", "Carlos", "Russell", "Bobby",
            "Eugene", "Todd", "Victor", "Martin", "Ernest", "Phillip", "Craig", "Alan", "Jesse", "Shawn", "Clarence",
            "Philip", "Chris", "Johnny", "Sean", "Earl", "Jimmy", "Antonio", "Danny", "Bryan", "Luis", "Mike",
            "Stanley", "Tony", "Nathan", "Dale", "Manuel", "Rodney", "Leonard", "Curtis", "Allen", "Marvin",
            "Vincent", "Norman", "Glenn", "Jeffery", "Travis", "Jeff", "Chad", "Jacob", "Lee", "Melvin", "Alfred",
            "Kyle", "Bradley", "Herbert", "Francis", "Jesus", "Frederick", "Ray", "Joel", "Edwin", "Don", "Eddie",
            "Troy", "Randall", "Barry", "Ricky", "Alexander", "Bernard", "Mario", "Leroy", "Micheal", "Francisco",
            "Marcus", "Theodore", "Clifford", "Miguel", "Oscar", "Jay", "Tom", "Jim", "Alex", "Jon", "Calvin",
            "Ronnie", "Bill", "Lloyd", "Tommy", "Leon", "Warren", "Derek", "Darrell", "Jerome", "Floyd", "Leo",
            "Tim", "Wesley", "Gordon", "Alvin", "Dean", "Greg", "Pedro", "Jorge", "Dustin", "Derrick", "Dan",
            "Lewis", "Zachary", "Corey", "Herman", "Vernon", "Roberto", "Clyde", "Maurice", "Glen", "Hector",
            "Shane", "Rick", "Ricardo", "Sam","Ortiz", "Gomez", "Murray", "Freeman", "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Hunter",
            "Hicks", "Porter", "Crawford", "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Dixon", "Ramos",
            "Reyes", "Burns", "Warren", "Gordon", "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black",
            "Daniels", "Palmer", "Mills", "Nichols", "Grant", "Knight", "Ferguson", "Rose", "Stone", "Dunn",
            "Hawkins", "Perkins", "Hudson", "Spencer", "Gardner", "Stephens", "Payne", "Pierce", "Berry",
            "Willis", "Ray", "Matthews", "Arnold", "Wagner", "Watkins", "Olson", "Carroll", "Duncan", "Snyder",
            "Hart", "Cunningham", "Bradley", "Lane", "Ruiz", "Andrews", "Harper", "Fox", "Riley", "Armstrong",
            "Carpenter", "Weaver", "Lawrence", "Elliott", "Chavez", "Greene", "Kelley", "Franklin", "Sims",
            "Austin", "Peters", "Lawson"};
    /*
    boolean isVowel = random.nextBoolean(); // Start with a vowel or consonant randomly
                int length = random.nextInt(5) + 2; // Generate a segment with length between 2 and 6
                StringBuilder segment = new StringBuilder();

                for (int j = 0; j < length; j++) {
                    if (isVowel) {
                        segment.append(VOWELS[random.nextInt(VOWELS.length)]);
                    } else {
                        segment.append(CONSONANTS[random.nextInt(CONSONANTS.length)]);
                    }
                    isVowel = !isVowel; // Toggle between vowel and consonant
                }
                name.append(segment.toString().substring(0, 1).toUpperCase() + segment.toString().substring(1)); // Capitalize the segment
    */
    public static String generateRandomName(int numSegments) {
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        Boolean IsUseRandomName = false;
        for (int i = 0; i < numSegments-1; i++) {
            if(random.nextBoolean()){
                //如果判断正确，就生成一个随机名字
                boolean isVowel = random.nextBoolean(); // Start with a vowel or consonant randomly
                int length = random.nextInt(5) + 2; // Generate a segment with length between 2 and 6
                StringBuilder segment = new StringBuilder();

                for (int j = 0; j < length; j++) {
                    if (isVowel) {
                        segment.append(VOWELS[random.nextInt(VOWELS.length)]);
                    } else {
                        segment.append(CONSONANTS[random.nextInt(CONSONANTS.length)]);
                    }
                    isVowel = !isVowel; // Toggle between vowel and consonant
                }
                name.append(segment.toString().substring(0, 1).toUpperCase() + segment.toString().substring(1)).append(" ");
                IsUseRandomName = true;//改变状态
            }else {
                // 否则就从中间名里随机找一个
                if (i == numSegments -2 && numSegments >2 && IsUseRandomName == false){
                    System.out.println("!");
                    // 如果到了倒数第二个的时候仍然没有使用
                    //如果判断正确，就生成一个随机名字
                    boolean isVowel = random.nextBoolean(); // Start with a vowel or consonant randomly
                    int length = random.nextInt(5) + 2; // Generate a segment with length between 2 and 6
                    StringBuilder segment = new StringBuilder();
                    for (int j = 0; j < length; j++) {
                        if (isVowel) {
                            segment.append(VOWELS[random.nextInt(VOWELS.length)]);
                        } else {
                            segment.append(CONSONANTS[random.nextInt(CONSONANTS.length)]);
                        }
                        isVowel = !isVowel; // Toggle between vowel and consonant
                    }
                    name.append(segment.toString().substring(0, 1).toUpperCase() + segment.toString().substring(1));
                    IsUseRandomName = true;//改变状态

                }else {
                    name.append(MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)]).append(" ");
                }

            }

        }
        // Add last name if more than one segment
        if (numSegments > 1) {
            name.append(LAST_NAMES[random.nextInt(LAST_NAMES.length)]).append(" ");
        }
        return name.toString();
    }
}
