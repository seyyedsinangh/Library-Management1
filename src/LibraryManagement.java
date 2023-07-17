import java.sql.Time;
import java.util.*;


public class LibraryManagement {
    public static void main(String[] args) {
        Management manager = new Management();
        String command;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            command = scanner.nextLine();
            if (command.equals("finish")) break;
            String[] splitted = command.split("#");
            switch (splitted[0]) {
                case "add-library":
                    splitted = splitted[1].split("\\|");
                    if (manager.searchLibrary(splitted[0])) {
                        System.out.println("duplicate-id");
                    } else {
                        Library library = new Library(splitted[0], splitted[1], splitted[2], Integer.parseInt(splitted[3]),
                                splitted[4]);
                        manager.getLibraries().put(splitted[0], library);
                        System.out.println("success");
                    }
                    break;
                case "add-category":
                    splitted = splitted[1].split("\\|");
                    if (manager.searchCategory(splitted[0])) {
                        System.out.println("duplicate-id");
                    } else {
                        Category category = new Category(splitted[0], splitted[1]);
                        manager.getCategories().put(splitted[0], category);
                        System.out.println("success");
                    }
                    break;
                case "add-book":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchCategory(splitted[6]) && manager.searchLibrary(splitted[7]))) {
                        System.out.println("not-found");
                    } else if (manager.searchPaper(splitted[7],splitted[0])==1) {
                        System.out.println("duplicate-id");
                    } else {
                        Book book = new Book(splitted[0], splitted[1], splitted[2], splitted[3], splitted[4],
                                Integer.parseInt(splitted[5]), splitted[6], splitted[7]);
                        manager.getLibraries().get(splitted[7]).getBooks().put(splitted[0],book);
                        System.out.println("success");
                    }
                    break;
                case "edit-book":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[1]) &&
                            (manager.searchPaper(splitted[1],splitted[0])==1) &&
                            (splitted[7].equals("-") || manager.searchCategory(splitted[7])))) {
                        System.out.println("not-found");
                    } else {
                        manager.getLibraries().get(splitted[1]).getBooks().get(splitted[0]).editBook(splitted[2],
                                splitted[3], splitted[4], splitted[5], splitted[6], splitted[7]);
                        System.out.println("success");
                    }
                    break;
                case "remove-book":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[1]) &&
                            (manager.searchPaper(splitted[1],splitted[0])==1))) {
                        System.out.println("not-found");
                    } else if (!manager.isRemoveBookAllowed(splitted[1],splitted[0])) {
                        System.out.println("not-allowed");
                    } else {
                        manager.getLibraries().get(splitted[1]).getBooks().remove(splitted[0]);
                        System.out.println("success");
                    }
                    break;
                case "add-thesis":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchCategory(splitted[5]) &&
                            manager.searchLibrary(splitted[6]))) {
                        System.out.println("not-found");
                    } else if (manager.searchPaper(splitted[6],splitted[0])==2) {
                        System.out.println("duplicate-id");
                    } else {
                        Thesis thesis = new Thesis(splitted[0],splitted[1],splitted[2],splitted[3],
                                splitted[4],splitted[5],splitted[6]);
                        manager.getLibraries().get(splitted[6]).getThesises().put(splitted[0],thesis);
                        System.out.println("success");
                    }
                    break;
                case "edit-thesis":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[1]) &&
                            manager.searchPaper(splitted[1],splitted[0])==2 &&
                            (splitted[7].equals("-") || manager.searchCategory(splitted[7])))) {
                        System.out.println("not-found");
                    } else {
                        manager.getLibraries().get(splitted[1]).getThesises().get(splitted[0]).editThesis(splitted[2],
                                splitted[3],splitted[4],splitted[5],splitted[6]);
                        System.out.println("success");
                    }
                    break;
                case "remove-thesis":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[1]) &&
                            (manager.searchPaper(splitted[1],splitted[0])==2))) {
                        System.out.println("not-found");
                    } else if (!manager.isRemoveBookAllowed(splitted[1],splitted[0])) {
                        System.out.println("not-allowed");
                    } else {
                        manager.getLibraries().get(splitted[1]).getThesises().remove(splitted[0]);
                        System.out.println("success");
                    }
                    break;
                case "add-student":
                    splitted = splitted[1].split("\\|");
                    if (manager.searchUser(splitted[0])) {
                        System.out.println("duplicate-id");
                    } else {
                        User student = new User(splitted[0],splitted[1],splitted[2],splitted[3],
                                splitted[4],splitted[5],splitted[6],"student");
                        manager.getUsers().put(splitted[0],student);
                        System.out.println("success");
                    }
                    break;
                case "edit-student", "edit-staff":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchUser(splitted[0])) {
                        System.out.println("not-found");
                    } else {
                        manager.getUsers().get(splitted[0]).editUser(splitted[1],splitted[2],splitted[3],
                                splitted[4],splitted[5],splitted[6]);
                        System.out.println("success");
                    }
                    break;
                case "remove-student", "remove-staff":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchUser(splitted[0])) {
                        System.out.println("not-found");
                    } else if (manager.getUsers().get(splitted[0]).getBorrowedCount()!=0 ||
                            manager.getUsers().get(splitted[0]).getPenalty()!=0) {
                        System.out.println("not-allowed");
                    } else {
                        manager.getUsers().remove(splitted[0]);
                        System.out.println("success");
                    }
                    break;
                case "add-staff":
                    splitted = splitted[1].split("\\|");
                    if (manager.searchUser(splitted[0])) {
                        System.out.println("duplicate-id");
                    } else {
                        User staff = new User(splitted[0],splitted[1],splitted[2],splitted[3],
                                splitted[4],splitted[5],splitted[6],"staff");
                        manager.getUsers().put(splitted[0],staff);
                        System.out.println("success");
                    }
                    break;
                case "borrow":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[2]) &&
                            (manager.searchPaper(splitted[2],splitted[3])!=0) && manager.searchUser(splitted[0]))) {
                        System.out.println("not-found");
                    } else if (!manager.getUsers().get(splitted[0]).getPassWord().equals(splitted[1])) {
                        System.out.println("invalid-pass");
                    } else if (!(manager.isBorrowAllowed(splitted[0],splitted[2],splitted[3]))) {
                        System.out.println("not-allowed");
                    } else {
                        Borrowed borrowed = new Borrowed(splitted[0],splitted[2],splitted[3],splitted[4],splitted[5]);
                        manager.getBorroweds().add(borrowed);
                        manager.getUsers().get(splitted[0]).changeCount("+");
                        if (manager.searchPaper(splitted[2],splitted[3])==1) {
                            manager.getLibraries().get(splitted[2]).getBooks().get(splitted[3]).changeCount("-");
                        } else {
                            manager.getLibraries().get(splitted[2]).getThesises().get(splitted[3]).take();
                        }
                        System.out.println("success");
                    }
                    break;
                case "return":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[2]) &&
                            (manager.searchPaper(splitted[2],splitted[3])!=0) && manager.searchUser(splitted[0]))) {
                        System.out.println("not-found");
                    } else if (!manager.getUsers().get(splitted[0]).getPassWord().equals(splitted[1])) {
                        System.out.println("invalid-pass");
                    } else {
                        Borrowed ret = new Borrowed(splitted[0],splitted[2],splitted[3],splitted[4],splitted[5]);
                        Borrowed bor = manager.searchBorrow(ret);
                        if (bor==null) {
                            System.out.println("not-found");
                        } else if (manager.getUsers().get(splitted[0]).getUserType().equals("staff")) {
                            long x = 14*24*3600*1000;
                            long y = 10*24*3600*1000;
                            long h = 3600*1000;
                            long timeDiff = ret.getDateTime().getTime() - bor.getDateTime().getTime();
                            if (manager.searchPaper(splitted[2],splitted[3])==1 && timeDiff>x && (timeDiff-x)/h!=0) {
                                manager.getUsers().get(splitted[0]).addPenalty((timeDiff-x)/h*100);
                                System.out.println((timeDiff-x)/h*100);
                            } else if (manager.searchPaper(splitted[2],splitted[3])==2 && timeDiff>y && (timeDiff-y)/h!=0) {
                                manager.getUsers().get(splitted[0]).addPenalty((timeDiff-y)/h*100);
                                System.out.println((timeDiff-y)/h*100);
                            } else {
                                System.out.println("success");
                            }
                            manager.getBorroweds().remove(bor);
                            manager.getUsers().get(splitted[0]).changeCount("-");
                            if (manager.searchPaper(splitted[2],splitted[3])==1) {
                                manager.getLibraries().get(splitted[2]).getBooks().get(splitted[3]).changeCount("+");
                            } else {
                                manager.getLibraries().get(splitted[2]).getThesises().get(splitted[3]).give();
                            }
                        } else {
                            long x = 10*24*3600*1000;
                            long y = 7*24*3600*1000;
                            long h = 3600*1000;
                            long timeDiff = ret.getDateTime().getTime() - bor.getDateTime().getTime();
                            if (manager.searchPaper(splitted[2],splitted[3])==1 && timeDiff>x && (timeDiff-x)/h!=0) {
                                manager.getUsers().get(splitted[0]).addPenalty((timeDiff-x)/h*50);
                                System.out.println((timeDiff-x)/h*50);
                            } else if (manager.searchPaper(splitted[2],splitted[3])==2 && timeDiff>y && (timeDiff-y)/h!=0){
                                manager.getUsers().get(splitted[0]).addPenalty((timeDiff-y)/h*50);
                                System.out.println((timeDiff-y)/h*50);
                            } else {
                                System.out.println("success");
                            }
                            manager.getBorroweds().remove(bor);
                            manager.getUsers().get(splitted[0]).changeCount("-");
                            if (manager.searchPaper(splitted[2],splitted[3])==1) {
                                manager.getLibraries().get(splitted[2]).getBooks().get(splitted[3]).changeCount("+");
                            } else {
                                manager.getLibraries().get(splitted[2]).getThesises().get(splitted[3]).give();
                            }
                        }
                    }
                    break;
                case "category-report":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchCategory(splitted[0])) {
                        System.out.println("not-found");
                    } else {
                        int bookCount = 0;
                        int thesisCount = 0;
                        for (String libraryId: manager.getLibraries().keySet()) {
                            for (Book book: manager.getLibraries().get(libraryId).getBooks().values()) {
                                if (book.getCategoryId().equals(splitted[0])) bookCount += book.getCopyConutFinal();
                            }
                            for (Thesis thesis: manager.getLibraries().get(libraryId).getThesises().values()) {
                                if (thesis.getCategoryId().equals(splitted[0])) thesisCount++;
                            }
                        }
                        System.out.println(bookCount + " " + thesisCount);
                    }
                    break;
                case "library-report":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchLibrary(splitted[0])) {
                        System.out.println("not-found");
                    } else {
                        int borrowedBookCount = 0;
                        int borrowedThesisCount = 0;
                        int bookCount = 0;
                        for (Borrowed borrowed: manager.getBorroweds()) {
                            if (!borrowed.getLibraryId().equals(splitted[0])) continue;
                            if (manager.searchPaper(splitted[0],borrowed.getBookId())==1) borrowedBookCount++;
                            else borrowedThesisCount++;
                        }
                        for (Book book: manager.getLibraries().get(splitted[0]).getBooks().values()) {
                            bookCount += book.getCopyConutFinal();
                        }
                        System.out.println(bookCount + " " +
                                manager.getLibraries().get(splitted[0]).getThesises().size() + " " +
                                borrowedBookCount + " " + borrowedThesisCount);
                    }
                    break;
                case "report-passed-deadline":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchLibrary(splitted[0])) {
                        System.out.println("not-found");
                    } else {
                        String[] splitted1 = splitted[1].split("-");
                        String[] splitted2 = splitted[2].split(":");
                        HashSet<String> result = new HashSet<String>();
                        Date date = new Date(Integer.parseInt(splitted1[0]),
                                Integer.parseInt(splitted1[1])-1,Integer.parseInt(splitted1[2]),
                                Integer.parseInt(splitted2[0]),Integer.parseInt(splitted2[1]));
                        for (Borrowed borrowed: manager.getBorroweds()) {
                            if (!borrowed.getLibraryId().equals(splitted[0])) {
                                continue;
                            }
                            if (manager.getUsers().get(borrowed.getUserId()).getUserType().equals("staff")) {
                                long x = 14*24*3600*1000;
                                long y = 10*24*3600*1000;
                                long timeDiff = date.getTime() - borrowed.getDateTime().getTime();
                                if (manager.searchPaper(borrowed.getLibraryId(),borrowed.getBookId())==1 && timeDiff>x) {
                                    result.add(borrowed.getBookId());
                                } else if (manager.searchPaper(borrowed.getLibraryId(),borrowed.getBookId())==2 && timeDiff>y) {
                                    result.add(borrowed.getBookId());
                                }
                            } else {
                                long x = 10*24*3600*1000;
                                long y = 7*24*3600*1000;
                                long timeDiff = date.getTime() - borrowed.getDateTime().getTime();
                                if (manager.searchPaper(borrowed.getLibraryId(),borrowed.getBookId())==1 && timeDiff>x) {
                                    result.add(borrowed.getBookId());
                                } else if (manager.searchPaper(borrowed.getLibraryId(),borrowed.getBookId())==2 && timeDiff>y) {
                                    result.add(borrowed.getBookId());
                                }
                            }
                        }
                        if (result.isEmpty()) {
                            System.out.println("none");
                        } else {
                            ArrayList<String> res = new ArrayList<String>();
                            for (String str : result) {
                                res.add(str);
                            }
                            Collections.sort(res);
                            for (int i = 0; i < res.size() - 1; i++) {
                                System.out.print(res.get(i) + "|");
                            }
                            System.out.println(res.get(res.size() - 1));
                        }
                    }
                    break;
                case "report-penalties-sum":
                    long sumPenalty = 0;
                    for (User user: manager.getUsers().values()) {
                        sumPenalty += user.getPenalty();
                    }
                    System.out.println(sumPenalty);
                    break;
                case "search-user":
                    splitted = splitted[1].split("\\|");
                    if (!manager.searchUser(splitted[0])) {
                        System.out.println("not-found");
                    } else if (!manager.getUsers().get(splitted[0]).getPassWord().equals(splitted[1])) {
                        System.out.println("invalid-pass");
                    } else {
                        HashSet<String> result = new HashSet<String>();
                        for (User user: manager.getUsers().values()) {
                            if (user.getFirstname().toLowerCase().contains(splitted[2].toLowerCase()) ||
                                    user.getLastname().toLowerCase().contains(splitted[2].toLowerCase())) {
                                result.add(user.getId());
                            }
                        }
                        if (result.isEmpty()) {
                            System.out.println("not-found");
                        } else {
                            ArrayList<String> res = new ArrayList<String>();
                            for (String str: result) {
                                res.add(str);
                            }
                            Collections.sort(res);
                            for (int i = 0; i < res.size()-1; i++) {
                                System.out.print(res.get(i) + "|");
                            }
                            System.out.println(res.get(res.size()-1));
                        }
                    }
                    break;
                case "search":
                    splitted = splitted[1].split("\\|");
                    HashSet<String> result = new HashSet<String>();
                    for (String libraryId: manager.getLibraries().keySet()) {
                        for (Book book: manager.getLibraries().get(libraryId).getBooks().values()) {
                            if (book.getTitle().toLowerCase().contains(splitted[0].toLowerCase()) ||
                                    book.getWriter().toLowerCase().contains(splitted[0].toLowerCase()) ||
                            book.getPublication().toLowerCase().contains(splitted[0].toLowerCase())) {
                                result.add(book.getId());
                            }
                        }
                        for (Thesis thesis: manager.getLibraries().get(libraryId).getThesises().values()) {
                            if (thesis.getTitle().toLowerCase().contains(splitted[0].toLowerCase()) ||
                            thesis.getProfessorName().toLowerCase().contains(splitted[0].toLowerCase()) ||
                            thesis.getStudentName().toLowerCase().contains(splitted[0].toLowerCase())) {
                                result.add(thesis.getId());
                            }
                        }
                    }
                    if (result.isEmpty()) {
                        System.out.println("not-found");
                    } else {
                        ArrayList<String> res = new ArrayList<String>();
                        for (String str: result) {
                            res.add(str);
                        }
                        Collections.sort(res);
                        for (int i = 0; i < res.size()-1; i++) {
                            System.out.print(res.get(i) + "|");
                        }
                        System.out.println(res.get(res.size()-1));
                    }
                    break;
                case "reserve-seat":
                    splitted = splitted[1].split("\\|");
                    if (!(manager.searchLibrary(splitted[2]) && manager.searchUser(splitted[0]))) {
                        System.out.println("not-found");
                    } else if (!manager.getUsers().get(splitted[0]).getPassWord().equals(splitted[1])) {
                        System.out.println("invalid-pass");
                    } else if (!manager.isReserveAllowed(splitted[0],splitted[3],splitted[4],splitted[5])) {
                        System.out.println("not-allowed");
                    } else if (!manager.isReserveAvailable(splitted[2],splitted[3],splitted[4],splitted[5])) {
                        System.out.println("not-available");
                    } else {
                        ReserveSeat reserveSeat = new ReserveSeat(splitted[0],splitted[2],splitted[3],splitted[4],splitted[5]);
                        manager.getReservedSeats().add(reserveSeat);
                        System.out.println("success");
                    }
                    break;
                default:
                    continue;
            }
        }
    }
}



class Management {
    private HashMap<String,Library> libraries;
    private HashMap<String,Category> categories;
    private HashMap<String,User> users;
    private HashSet<Borrowed> borroweds;
    private HashSet<ReserveSeat> reservedSeats;


    public Management() {
        libraries = new HashMap<String,Library>();
        categories = new HashMap<String,Category>();
        Category category = new Category("null","null");
        categories.put("null",category);
        users = new HashMap<String,User>();
        borroweds = new HashSet<Borrowed>();
        reservedSeats = new HashSet<ReserveSeat>();
    }

    public boolean searchLibrary(String id) {
        return libraries.containsKey(id);
    }

    public boolean searchCategory(String id) {
        return categories.containsKey(id);
    }

    public boolean searchUser(String id) {
        return users.containsKey(id);
    }

    //0 = not exist , 1 = book , 2 = thesis
    public int searchPaper(String libId,String paperId) {
        if (libraries.get(libId).getBooks().containsKey(paperId)) {
            return 1;
        } else if (libraries.get(libId).getThesises().containsKey(paperId)) {
            return 2;
        } else {
            return 0;
        }
    }

    public boolean isBorrowAllowed(String userId,String libId,String paperId) {
        switch (users.get(userId).getUserType()) {
            case "student":
                if (users.get(userId).getBorrowedCount()==3) {
                    return false;
                }
                break;
            case "staff":
                if (users.get(userId).getBorrowedCount()==5) {
                    return false;
                }
                break;
        }
        switch (searchPaper(libId,paperId)) {
            case 1:
                if (libraries.get(libId).getBooks().get(paperId).getCopyCount()==0) {
                    return false;
                } else {
                    return true;
                }
            case 2:
                if (libraries.get(libId).getThesises().get(paperId).isTaken()) {
                    return false;
                } else {
                    return true;
                }
            default:
                return false;
        }
    }

    public boolean isReserveAvailable(String libraryId,String date,String start,String end) {
        String[] splittedDate = date.split("-");
        String[] splittedStart = start.split(":");
        String[] splittedEnd = end.split(":");
        Time startTime = new Time(Integer.parseInt(splittedStart[0]),Integer.parseInt(splittedStart[1]),0);
        Time endTime = new Time(Integer.parseInt(splittedEnd[0]),Integer.parseInt(splittedEnd[1]),0);
        Date dateReserve = new Date(Integer.parseInt(splittedDate[0]),
                Integer.parseInt(splittedDate[1])-1,Integer.parseInt(splittedDate[2]));
        int temp = 0;
        for (ReserveSeat reserveSeat: reservedSeats) {
            if (reserveSeat.getLibraryId().equals(libraryId) &&
                    reserveSeat.getDate().getTime()==dateReserve.getTime() &&
                    !(endTime.getTime()<=reserveSeat.getStart().getTime() ||
                            startTime.getTime()>=reserveSeat.getEnd().getTime())) {
                temp++;
            }
        }
        if (temp==libraries.get(libraryId).getTableCount()) {
            return false;
        }
        return true;
    }

    public boolean isReserveAllowed(String userId,String date,String start,String end) {
        String[] splittedDate = date.split("-");
        String[] splittedStart = start.split(":");
        String[] splittedEnd = end.split(":");
        Time startTime = new Time(Integer.parseInt(splittedStart[0]),Integer.parseInt(splittedStart[1]),0);
        Time endTime = new Time(Integer.parseInt(splittedEnd[0]),Integer.parseInt(splittedEnd[1]),0);
        long h = 8*3600*1000;
        if (endTime.getTime()-startTime.getTime()>h) {
            return false;
        }
        Date dateReserve = new Date(Integer.parseInt(splittedDate[0]),
                Integer.parseInt(splittedDate[1])-1,Integer.parseInt(splittedDate[2]));
        for (ReserveSeat reserveSeat: reservedSeats) {
            if (reserveSeat.getUserId().equals(userId) && reserveSeat.getDate().getTime()==dateReserve.getTime()) {
                return false;
            }
        }
        return true;
    }

    public Borrowed searchBorrow(Borrowed borrowed) {
        boolean flag = false;
        Borrowed res = null;
        for (Borrowed bor: borroweds) {
            if (borrowed.getBookId().equals(bor.getBookId()) &&
                    borrowed.getLibraryId().equals(bor.getLibraryId()) &&
                    borrowed.getUserId().equals(bor.getUserId())) {
                if (flag) {
                    if (bor.getDateTime().getTime()<res.getDateTime().getTime()) {
                        res = bor;
                    }
                } else {
                    res = bor;
                }
                flag = true;
            }
        }
        return res;
    }

    public boolean isRemoveBookAllowed(String libId,String bookId) {
        for (Borrowed bor: borroweds) {
            if (bor.getLibraryId().equals(libId) && bor.getBookId().equals(bookId)) {
                return false;
            }
        }
        return true;
    }


    public HashMap<String, Library> getLibraries() {
        return libraries;
    }

    public HashMap<String, Category> getCategories() {
        return categories;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public HashSet<Borrowed> getBorroweds() {
        return borroweds;
    }

    public HashSet<ReserveSeat> getReservedSeats() {
        return reservedSeats;
    }
}



class Borrowed {
    private String userId;
    private String libraryId;
    private String bookId;
    private Date dateTime;

    public Borrowed(String userId, String libraryId, String bookId, String date, String time) {
        this.userId = userId;
        this.libraryId = libraryId;
        this.bookId = bookId;
        String[] splitted1 = date.split("-");
        String[] splitted2 = time.split(":");
        this.dateTime = new Date(Integer.parseInt(splitted1[0]),Integer.parseInt(splitted1[1])-1,
                Integer.parseInt(splitted1[2]),Integer.parseInt(splitted2[0]),Integer.parseInt(splitted2[1]));
    }

    public String getUserId() {
        return userId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public String getBookId() {
        return bookId;
    }

    public Date getDateTime() {
        return dateTime;
    }
}



class Thesis {
    private String id;
    private String title;
    private String studentName;
    private String professorName;
    private String defenseYear;
    private String categoryId;
    private String libraryId;
    private boolean isTaken;

    public Thesis(String id, String title, String studentName, String professorName, String defenseYear, String categoryId, String libraryId) {
        this.id = id;
        this.title = title;
        this.studentName = studentName;
        this.professorName = professorName;
        this.defenseYear = defenseYear;
        this.categoryId = categoryId;
        this.libraryId = libraryId;
        this.isTaken = false;
    }

    public void editThesis(String title,String studentName,String professorName,String defenseYear,String categoryId) {
        if (isDash(title)) this.title = title;
        if (isDash(studentName)) this.studentName = studentName;
        if (isDash(professorName)) this.professorName = professorName;
        if (isDash(defenseYear)) this.defenseYear = defenseYear;
        if (isDash(categoryId)) this.categoryId = categoryId;
    }

    private boolean isDash(String str) {
        if (str.equals("-")) return false;
        else return true;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getId() {
        return id;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void take() {
        isTaken = true;
    }

    public void give() {
        isTaken = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thesis thesis = (Thesis) o;
        return Objects.equals(id, thesis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



class User {
    private String id;
    private String passWord;
    private String firstname;
    private String lastname;
    private String nationalCode;
    private String birthYear;
    private String address;
    private String userType;
    private int borrowedCount;
    private long penalty;

    public User(String id, String passWord, String firstname, String lastname, String nationalCode, String birthYear, String address, String userType) {
        this.id = id;
        this.passWord = passWord;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationalCode = nationalCode;
        this.birthYear = birthYear;
        this.address = address;
        this.userType = userType;
        this.borrowedCount = 0;
        this.penalty = 0;
    }

    public void editUser(String passWord,String firstname,String lastname,String nationalCode,String birthYear,String address) {
        if (isDash(passWord)) this.passWord = passWord;
        if (isDash(firstname)) this.firstname = firstname;
        if (isDash(lastname)) this.lastname = lastname;
        if (isDash(nationalCode)) this.nationalCode = nationalCode;
        if (isDash(birthYear)) this.birthYear = birthYear;
        if (isDash(address)) this.address = address;
    }

    private boolean isDash(String str) {
        if (str.equals("-")) return false;
        else return true;
    }

    public String getId() {
        return id;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserType() {
        return userType;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public long getPenalty() {
        return penalty;
    }

    public void changeCount(String type) {
        if (type.equals("+")) this.borrowedCount++;
        if (type.equals("-")) this.borrowedCount--;
    }

    public void addPenalty(long penalty) {
        this.penalty += penalty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(passWord, user.passWord) && Objects.equals(userType, user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passWord, userType);
    }
}


class Book {
    private String id;
    private String title;
    private String writer;
    private String publication;
    private String publishYear;
    private int copyCount;
    private int copyConutFinal;
    private String categoryId;
    private String libraryId;

    public Book(String id, String title, String writer, String publication, String publishYear, int copyCount, String category, String library) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.publication = publication;
        this.publishYear = publishYear;
        this.copyCount = copyCount;
        this.copyConutFinal = copyCount;
        this.categoryId = category;
        this.libraryId = library;
    }

    public void editBook(String title,String writer,String publication,String publishYear,String copyCount,String categoryId) {
        if (isDash(title)) this.title = title;
        if (isDash(writer)) this.writer = writer;
        if (isDash(publication)) this.publication = publication;
        if (isDash(publishYear)) this.publishYear = publishYear;
        if (isDash(copyCount)) {
            this.copyCount = Integer.parseInt(copyCount);
            this.copyConutFinal = Integer.parseInt(copyCount); 
        }
        if (isDash(categoryId)) this.categoryId = categoryId;
    }

    public void changeCount(String type) {
        if (type.equals("+")) this.copyCount++;
        if (type.equals("-")) this.copyCount--;
    }

    public int getCopyCount() {
        return copyCount;
    }

    public int getCopyConutFinal() {
        return copyConutFinal;
    }

    private boolean isDash(String str) {
        if (str.equals("-")) return false;
        else return true;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getPublication() {
        return publication;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


class Category {
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


class Library {
    private String id;
    private String name;
    private String establishYear;
    private int tableCount;
    private String address;
    private HashMap<String,Book> books;
    private HashMap<String,Thesis> thesises;

    public Library(String id, String name, String establishYear, int tableCount, String address) {
        this.id = id;
        this.name = name;
        this.establishYear = establishYear;
        this.tableCount = tableCount;
        this.address = address;
        this.books = new HashMap<String,Book>();
        this.thesises = new HashMap<String,Thesis>();
    }

    public int getTableCount() {
        return tableCount;
    }

    public HashMap<String,Book> getBooks() {
        return books;
    }

    public HashMap<String, Thesis> getThesises() {
        return thesises;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(id, library.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


class ReserveSeat {
    private String userId;
    private String libraryId;
    private Date date;
    private Time start;
    private Time end;


    public ReserveSeat(String userId, String libraryId, String date, String start, String end) {
        this.userId = userId;
        this.libraryId = libraryId;
        String[] splitted1 = date.split("-");
        String[] splitted2 = start.split(":");
        String[] splitted3 = end.split(":");
        this.date = new Date(Integer.parseInt(splitted1[0]),Integer.parseInt(splitted1[1])-1,
                Integer.parseInt(splitted1[2]));
        this.start = new Time(Integer.parseInt(splitted2[0]),Integer.parseInt(splitted2[1]),0);
        this.end = new Time(Integer.parseInt(splitted3[0]),Integer.parseInt(splitted3[1]),0);
    }

    public String getUserId() {
        return userId;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public Date getDate() {
        return date;
    }

    public Time getStart() {
        return start;
    }

    public Time getEnd() {
        return end;
    }
}