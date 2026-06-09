import { useState, useEffect, useRef } from 'react';
import { 
  BookOpen, 
  Star, 
  Plus, 
  Edit, 
  Trash, 
  Search, 
  Shield, 
  User, 
  Sun, 
  Moon, 
  Database, 
  Import, 
  RefreshCw, 
  X, 
  FileText, 
  Globe, 
  Layers, 
  AlertCircle,
  Hash,
  ShoppingBag,
  Clock,
  ChevronDown,
  ChevronUp,
  Settings,
  SlidersHorizontal,
  LogOut,
  UserCheck,
  Languages,
  ArrowLeft
} from 'lucide-react';

// Expandable Mock Genres List
const ALL_GENRES = [
  "Science Fiction", 
  "Fantasy", 
  "Biography", 
  "Self-Help", 
  "Technology", 
  "Classics", 
  "Mystery", 
  "Thriller", 
  "Romance", 
  "History", 
  "Drama", 
  "Business", 
  "Philosophy", 
  "Art"
];

const COMMON_GENRES = ["Science Fiction", "Fantasy", "Biography", "Technology"];
const COMMON_AUTHORS = ["Frank Herbert", "J.R.R. Tolkien", "Tara Westover", "James Clear"];

// Mock Books Data (Diverse for filter combinations)
const initialBooks = [
  {
    id: 1,
    isbn: "9780441172719",
    title: "Dune",
    author: "Frank Herbert",
    pagecount: 604,
    publisher: "Chilton Books",
    genre: "Science Fiction",
    price: 14.99,
    language: "English",
    description: "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world where the only thing of value is the 'spice' melange, a drug capable of extending life and enhancing consciousness.",
    publishingDate: "1965-08-01",
    averageRating: 4.8,
    ratingCount: 3
  },
  {
    id: 2,
    isbn: "9780261103573",
    title: "The Hobbit",
    author: "J.R.R. Tolkien",
    pagecount: 310,
    publisher: "George Allen & Unwin",
    genre: "Fantasy",
    price: 9.99,
    language: "English",
    description: "The Hobbit is a children's fantasy novel by J.R.R. Tolkien. It follows the quest of home-loving hobbit Bilbo Baggins to win a share of the treasure guarded by Smaug the dragon.",
    publishingDate: "1937-09-21",
    averageRating: 4.5,
    ratingCount: 2
  },
  {
    id: 3,
    isbn: "9780399592522",
    title: "Educated",
    author: "Tara Westover",
    pagecount: 352,
    publisher: "Random House",
    genre: "Biography",
    price: 12.50,
    language: "English",
    description: "An unforgettable memoir about a young girl who, kept out of school, leaves her survivalist family and goes on to earn a PhD from Cambridge University.",
    publishingDate: "2018-02-20",
    averageRating: 5.0,
    ratingCount: 1
  },
  {
    id: 4,
    isbn: "9781847941831",
    title: "Atomic Habits",
    author: "James Clear",
    pagecount: 320,
    publisher: "Penguin Random House",
    genre: "Self-Help",
    price: 16.99,
    language: "English",
    description: "No matter your goals, Atomic Habits offers a proven framework for improving—every day. James Clear, one of the world's leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results.",
    publishingDate: "2018-10-16",
    averageRating: 5.0,
    ratingCount: 2
  },
  {
    id: 5,
    isbn: "9780132350884",
    title: "Clean Code",
    author: "Robert C. Martin",
    pagecount: 464,
    publisher: "Prentice Hall",
    genre: "Technology",
    price: 38.95,
    language: "English",
    description: "Even bad code can function. But if code isn't clean, it can bring a development organization to its knees. Every year, countless hours and significant resources are lost because of poorly written code. But it doesn't have to be that way. Noted software expert Robert C. Martin presents a revolutionary paradigm with Clean Code: A Handbook of Agile Software Craftsmanship.",
    publishingDate: "2008-08-01",
    averageRating: 4.5,
    ratingCount: 2
  },
  {
    id: 6,
    isbn: "9780743273565",
    title: "The Great Gatsby",
    author: "F. Scott Fitzgerald",
    pagecount: 180,
    publisher: "Charles Scribner's Sons",
    genre: "Classics",
    price: 7.99,
    language: "English",
    description: "The Great Gatsby, F. Scott Fitzgerald's third book, stands as the supreme achievement of his career. First published in 1925, this quintessential novel of the Jazz Age has been acclaimed by generations of readers which tells the story of the mysteriously wealthy Jay Gatsby.",
    publishingDate: "1925-04-10",
    averageRating: 4.0,
    ratingCount: 2
  },
  {
    id: 7,
    isbn: "9781250175953",
    title: "The Silent Patient",
    author: "Alex Michaelides",
    pagecount: 336,
    publisher: "Celadon Books",
    genre: "Thriller",
    price: 10.99,
    language: "English",
    description: "The Silent Patient is a shocking psychological thriller of a woman's act of violence against her husband—and of the therapist obsessed with uncovering her motive.",
    publishingDate: "2019-02-05",
    averageRating: 4.3,
    ratingCount: 2
  },
  {
    id: 8,
    isbn: "9780143111831",
    title: "Thinking, Fast and Slow",
    author: "Daniel Kahneman",
    pagecount: 499,
    publisher: "Farrar, Straus and Giroux",
    genre: "Philosophy",
    price: 18.00,
    language: "English",
    description: "Daniel Kahneman, recipient of the Nobel Prize in Economic Sciences, takes us on a groundbreaking tour of the mind and explains the two systems that drive the way we think.",
    publishingDate: "2011-10-25",
    averageRating: 4.1,
    ratingCount: 1
  },
  {
    id: 9,
    isbn: "9780062315007",
    title: "The Alchemist",
    author: "Paulo Coelho",
    pagecount: 208,
    publisher: "HarperOne",
    genre: "Drama",
    price: 8.50,
    language: "Spanish",
    description: "Santiago, un joven pastor andaluz, emprende un viaje por el desierto egipcio en busca de un tesoro oculto en las pirámides.",
    publishingDate: "1988-01-01",
    averageRating: 4.7,
    ratingCount: 2
  },
  {
    id: 10,
    isbn: "9783150000014",
    title: "Faust: Eine Tragödie",
    author: "Johann Wolfgang von Goethe",
    pagecount: 144,
    publisher: "Reclam",
    genre: "Classics",
    price: 4.99,
    language: "German",
    description: "Faust ist ein Drama von Johann Wolfgang von Goethe. Es gilt als das bedeutendste Werk der deutschsprachigen Literatur.",
    publishingDate: "1808-04-12",
    averageRating: 4.6,
    ratingCount: 1
  }
];

const initialRatings = [
  { id: 1, rating: 5, comment: "An absolute masterpiece. The world-building is second to none.", creationTime: "2026-05-10T14:32:00Z", userId: 1, bookId: 1 },
  { id: 2, rating: 4, comment: "Incredible story and philosophy, but a bit slow paced.", creationTime: "2026-05-15T09:12:00Z", userId: 2, bookId: 1 },
  { id: 3, rating: 5, comment: "I've read this three times. The spice must flow!", creationTime: "2026-06-01T18:22:00Z", userId: 3, bookId: 1 },
  { id: 4, rating: 5, comment: "My favorite book of all time. Simple, magical, adventure.", creationTime: "2026-04-20T11:05:00Z", userId: 2, bookId: 2 },
  { id: 5, rating: 4, comment: "A classic adventure that remains delightful decades later.", creationTime: "2026-05-22T15:44:00Z", userId: 1, bookId: 2 },
  { id: 6, rating: 5, comment: "An inspiring memoir of resilience.", creationTime: "2026-06-03T10:15:00Z", userId: 1, bookId: 3 },
  { id: 7, rating: 5, comment: "Incredible practical advice.", creationTime: "2026-05-11T08:30:00Z", userId: 2, bookId: 4 },
  { id: 8, rating: 5, comment: "One of the most useful self-improvement books ever written.", creationTime: "2026-05-29T16:00:00Z", userId: 3, bookId: 4 },
  { id: 9, rating: 4, comment: "Essential reading for any software engineer.", creationTime: "2026-05-01T12:00:00Z", userId: 2, bookId: 5 },
  { id: 10, rating: 5, comment: "Made me a much better developer.", creationTime: "2026-06-05T21:40:00Z", userId: 1, bookId: 5 },
  { id: 11, rating: 4, comment: "Beautiful prose and a tragic story.", creationTime: "2026-04-18T17:50:00Z", userId: 1, bookId: 6 },
  { id: 12, rating: 4, comment: "A solid classic.", creationTime: "2026-05-12T14:10:00Z", userId: 2, bookId: 6 },
  { id: 13, rating: 4, comment: "Shocking twist at the end. Kept me up all night.", creationTime: "2026-05-20T22:15:00Z", userId: 1, bookId: 7 },
  { id: 14, rating: 5, comment: "Highly intellectual. Great look into psychological biases.", creationTime: "2026-06-02T13:40:00Z", userId: 2, bookId: 8 },
  { id: 15, rating: 5, comment: "Una historia maravillosa sobre seguir tus sueños.", creationTime: "2026-04-30T10:30:00Z", userId: 1, bookId: 9 },
  { id: 16, rating: 5, comment: "Der absolute Klassiker der Weltliteratur.", creationTime: "2026-05-18T11:00:00Z", userId: 2, bookId: 10 }
];

const users = [
  { id: 1, username: "sarah_read", email: "sarah@example.com", role: "USER", avatar: "S" },
  { id: 2, username: "john_doe", email: "john@example.com", role: "USER", avatar: "J" },
  { id: 3, username: "admin_mike", email: "mike@bookly.com", role: "ADMIN", avatar: "M" },
  { id: 4, username: "guest_visitor", email: "guest@example.com", role: "GUEST", avatar: "G" }
];

const emptyBookForm = {
  title: "",
  author: "",
  isbn: "",
  genre: "Science Fiction",
  price: "",
  pagecount: "",
  publisher: "",
  publishingDate: "",
  language: "English",
  description: ""
};

function App() {
  const [activeTab, setActiveTab] = useState('explore');
  const [selectedBook, setSelectedBook] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [theme, setTheme] = useState('dark');
  const [activeUser, setActiveUser] = useState(() => {
    const saved = localStorage.getItem('bookly_active_user');
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        return null;
      }
    }
    return null;
  });
  const [showProfileDropdown, setShowProfileDropdown] = useState(false);
  const [authMode, setAuthMode] = useState('login'); // 'login' or 'register'
  const [usernameInput, setUsernameInput] = useState('');
  const [emailInput, setEmailInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');
  const [registeredUsers, setRegisteredUsers] = useState(() => {
    const saved = localStorage.getItem('bookly_registered_users');
    if (saved) {
      try {
        return JSON.parse(saved);
      } catch (e) {
        return users;
      }
    }
    return users;
  });

  useEffect(() => {
    localStorage.setItem('bookly_registered_users', JSON.stringify(registeredUsers));
  }, [registeredUsers]);

  useEffect(() => {
    if (activeUser) {
      localStorage.setItem('bookly_active_user', JSON.stringify(activeUser));
    } else {
      localStorage.removeItem('bookly_active_user');
    }
  }, [activeUser]);
  const [books, setBooks] = useState(initialBooks);
  const [ratings, setRatings] = useState(initialRatings);
  const [toasts, setToasts] = useState([]);
  
  // Sorting State
  const [sortBy, setSortBy] = useState('rating');

  // Collapsible Filters State
  const [collapsibles, setCollapsibles] = useState({
    genre: true,
    author: true,
    price: true,
    language: true,
    rating: false
  });

  // Filter Values State
  const [selectedGenres, setSelectedGenres] = useState([]);
  const [selectedAuthors, setSelectedAuthors] = useState([]);
  const [minPriceFilter, setMinPriceFilter] = useState(0);
  const [maxPriceFilter, setMaxPriceFilter] = useState(100);
  const [selectedLanguages, setSelectedLanguages] = useState([]);
  const [minRatingFilter, setMinRatingFilter] = useState(0); // 0.0 to 5.0

  // "More Genres" Dialog state
  const [showGenreDialog, setShowGenreDialog] = useState(false);
  const [dialogSearch, setDialogSearch] = useState('');
  const [dialogSelectedGenres, setDialogSelectedGenres] = useState([]);

  // "More Authors" Dialog state
  const [showAuthorDialog, setShowAuthorDialog] = useState(false);
  const [dialogSearchAuthor, setDialogSearchAuthor] = useState('');
  const [dialogSelectedAuthors, setDialogSelectedAuthors] = useState([]);

  // Create / Edit Book Modal State
  const [showModal, setShowModal] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [newBookForm, setNewBookForm] = useState(emptyBookForm);
  
  // Rating Submission State
  const [newRating, setNewRating] = useState({ rating: 5, comment: "" });
  
  // OpenLibrary API State
  const [enriching, setEnriching] = useState(false);
  const [olQuery, setOlQuery] = useState('');
  const [olResults, setOlResults] = useState([]);
  const [olLoading, setOlLoading] = useState(false);
  const [olError, setOlError] = useState(null);
  const [isFilterDrawerOpen, setIsFilterDrawerOpen] = useState(false);

  // Dropdown reference to close when clicked outside
  const dropdownRef = useRef(null);

  // Sync Theme
  useEffect(() => {
    document.documentElement.className = theme;
  }, [theme]);

  // Click outside profile dropdown handler
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowProfileDropdown(false);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const addToast = (message, type = 'success') => {
    const id = Date.now();
    setToasts(prev => [...prev, { id, message, type }]);
    setTimeout(() => {
      setToasts(prev => prev.filter(t => t.id !== id));
    }, 4000);
  };

  const handleLogin = (e) => {
    e.preventDefault();
    if (!usernameInput.trim() || !passwordInput.trim()) {
      addToast("Please fill in all fields", "error");
      return;
    }
    const foundUser = registeredUsers.find(u => 
      u.username.toLowerCase() === usernameInput.trim().toLowerCase() ||
      u.email.toLowerCase() === usernameInput.trim().toLowerCase()
    );
    
    if (foundUser) {
      setActiveUser(foundUser);
      addToast(`Welcome back, ${foundUser.username}!`, "success");
      setUsernameInput('');
      setPasswordInput('');
    } else {
      addToast("User not found. (Use john_doe, sarah_read, admin_mike)", "error");
    }
  };

  const handleRegister = (e) => {
    e.preventDefault();
    if (!usernameInput.trim() || !emailInput.trim() || !passwordInput.trim()) {
      addToast("Please fill in all fields", "error");
      return;
    }
    const exists = registeredUsers.some(u => 
      u.username.toLowerCase() === usernameInput.trim().toLowerCase() ||
      u.email.toLowerCase() === emailInput.trim().toLowerCase()
    );
    if (exists) {
      addToast("Username or Email already taken", "error");
      return;
    }
    const newUser = {
      id: registeredUsers.length + 1,
      username: usernameInput.trim(),
      email: emailInput.trim(),
      role: "USER",
      avatar: usernameInput.trim().charAt(0).toUpperCase()
    };
    setRegisteredUsers([...registeredUsers, newUser]);
    setActiveUser(newUser);
    addToast("Account created successfully!", "success");
    setUsernameInput('');
    setEmailInput('');
    setPasswordInput('');
  };

  const handleGuestLogin = () => {
    const guestUser = registeredUsers.find(u => u.role === "GUEST") || users[3];
    setActiveUser(guestUser);
    addToast("Logged in as Guest", "info");
  };

  // Toggle collapsible filter sections
  const toggleCollapsible = (key) => {
    setCollapsibles(prev => ({ ...prev, [key]: !prev[key] }));
  };

  // Genre selection handler (sidebar checkboxes)
  const handleGenreCheckbox = (genre) => {
    setSelectedGenres(prev => 
      prev.includes(genre) ? prev.filter(g => g !== genre) : [...prev, genre]
    );
  };

  // Author selection handler
  const handleAuthorCheckbox = (author) => {
    setSelectedAuthors(prev =>
      prev.includes(author) ? prev.filter(a => a !== author) : [...prev, author]
    );
  };

  // Language selection handler
  const handleLanguageCheckbox = (lang) => {
    setSelectedLanguages(prev => 
      prev.includes(lang) ? prev.filter(l => l !== lang) : [...prev, lang]
    );
  };

  // Clear all filters
  const handleClearFilters = () => {
    setSelectedGenres([]);
    setSelectedAuthors([]);
    setMinPriceFilter(0);
    setMaxPriceFilter(100);
    setSelectedLanguages([]);
    setMinRatingFilter(0);
    setSearchQuery('');
    addToast("Filters reset", "info");
  };

  // Open the Genre selection dialog
  const openGenreDialog = () => {
    setDialogSelectedGenres([...selectedGenres]);
    setDialogSearch('');
    setShowGenreDialog(true);
  };

  // Apply genres selected in the dialog
  const applyDialogGenres = () => {
    setSelectedGenres(dialogSelectedGenres);
    setShowGenreDialog(false);
    addToast("Genre filters updated");
  };

  // Open the Author selection dialog
  const openAuthorDialog = () => {
    setDialogSelectedAuthors([...selectedAuthors]);
    setDialogSearchAuthor('');
    setShowAuthorDialog(true);
  };

  // Apply authors selected in the dialog
  const applyDialogAuthors = () => {
    setSelectedAuthors(dialogSelectedAuthors);
    setShowAuthorDialog(false);
    addToast("Author filters updated");
  };

  // Filter books using a combined filter logic
  const filteredBooks = books.filter(book => {
    // 1. Search Query (Matches Title, Author, or ISBN)
    const matchesSearch = 
      book.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
      book.author.toLowerCase().includes(searchQuery.toLowerCase()) ||
      book.isbn.includes(searchQuery);

    // 2. Genre Multi-select
    const matchesGenre = selectedGenres.length === 0 || selectedGenres.includes(book.genre);

    // 2.5. Author Multi-select
    const matchesAuthor = selectedAuthors.length === 0 || selectedAuthors.includes(book.author);

    // 3. Language Multi-select
    const matchesLanguage = selectedLanguages.length === 0 || selectedLanguages.includes(book.language);

    // 4. Price Ranges
    const matchesPrice = book.price >= minPriceFilter && book.price <= maxPriceFilter;

    // 5. Min Rating
    const matchesRating = book.averageRating >= minRatingFilter;

    return matchesSearch && matchesGenre && matchesAuthor && matchesLanguage && matchesPrice && matchesRating;
  });

  const sortedBooks = [...filteredBooks].sort((a, b) => {
    if (sortBy === 'rating') {
      return b.averageRating - a.averageRating;
    }
    if (sortBy === 'reviews') {
      return b.ratingCount - a.ratingCount;
    }
    if (sortBy === 'title') {
      return a.title.localeCompare(b.title);
    }
    if (sortBy === 'price-low') {
      return a.price - b.price;
    }
    if (sortBy === 'price-high') {
      return b.price - a.price;
    }
    return 0;
  });

  // OpenLibrary Book Auto-Enrichment function
  const enrichBookFromOpenLibrary = async (isbnVal) => {
    if (!isbnVal || isbnVal.trim().length < 9) return;
    setEnriching(true);
    try {
      const response = await fetch(`https://openlibrary.org/search.json?q=${encodeURIComponent(isbnVal)}&limit=1`);
      if (response.ok) {
        const data = await response.json();
        const doc = data.docs?.[0];
        if (doc) {
          const title = doc.title || "";
          const author = doc.author_name ? doc.author_name.join(', ') : "";
          const pagecount = doc.number_of_pages_median || doc.number_of_pages || "";
          const publisher = doc.publisher ? doc.publisher[0] : "";
          const firstPublishYear = doc.first_publish_year ? String(doc.first_publish_year) : "";
          const languageRaw = doc.language ? doc.language[0] : "eng";
          const language = languageRaw === 'eng' ? 'English' : languageRaw === 'ger' ? 'German' : languageRaw === 'spa' ? 'Spanish' : 'English';
          const description = doc.first_sentence ? doc.first_sentence[0] : (doc.edition_key ? `Imported from OpenLibrary.` : "");

          setNewBookForm(prev => ({
            ...prev,
            title: prev.title || title,
            author: prev.author || author,
            pagecount: prev.pagecount || String(pagecount),
            publisher: prev.publisher || publisher,
            publishingDate: prev.publishingDate || (firstPublishYear ? `${firstPublishYear}-01-01` : ""),
            language: prev.language || language,
            description: prev.description || description
          }));
          addToast("Book details enriched from OpenLibrary!", "success");
        } else {
          addToast("No details found for this ISBN on OpenLibrary.", "info");
        }
      }
    } catch (err) {
      console.error("OpenLibrary enrichment failed", err);
      addToast("Failed to enrich details from OpenLibrary API.", "error");
    } finally {
      setEnriching(false);
    }
  };

  // Dynamic system statistics
  const totalBooksCount = books.length;
  const avgSystemRating = (
    ratings.reduce((sum, r) => sum + r.rating, 0) / (ratings.length || 1)
  ).toFixed(1);
  const uniqueGenresCount = new Set(books.map(b => b.genre)).size;

  // Review Form Submit Handler
  const handleAddRating = (e) => {
    e.preventDefault();
    if (activeUser.role === 'GUEST') {
      addToast("Guests cannot submit reviews", "error");
      return;
    }
    if (!newRating.comment.trim()) {
      addToast("Please write a comment", "error");
      return;
    }

    const ratingId = Date.now();
    const ratingObj = {
      id: ratingId,
      rating: newRating.rating,
      comment: newRating.comment,
      creationTime: new Date().toISOString(),
      userId: activeUser.id,
      bookId: selectedBook.id
    };

    const updatedRatings = [ratingObj, ...ratings];
    setRatings(updatedRatings);

    // Update book ratings metadata
    const bookRatings = updatedRatings.filter(r => r.bookId === selectedBook.id);
    const avg = parseFloat((bookRatings.reduce((sum, r) => sum + r.rating, 0) / bookRatings.length).toFixed(1));

    const updatedBooks = books.map(b => {
      if (b.id === selectedBook.id) {
        return { ...b, averageRating: avg, ratingCount: bookRatings.length };
      }
      return b;
    });

    setBooks(updatedBooks);
    setSelectedBook(prev => ({ ...prev, averageRating: avg, ratingCount: bookRatings.length }));
    setNewRating({ rating: 5, comment: "" });
    addToast("Review added successfully!");
  };

  // Review Deletion Handler
  const handleDeleteRating = (ratingId) => {
    const ratingToDelete = ratings.find(r => r.id === ratingId);
    if (!ratingToDelete) return;

    if (activeUser.role !== 'ADMIN' && ratingToDelete.userId !== activeUser.id) {
      addToast("Unauthorized to delete this review", "error");
      return;
    }

    const updatedRatings = ratings.filter(r => r.id !== ratingId);
    setRatings(updatedRatings);

    // Update book ratings metadata
    const bookRatings = updatedRatings.filter(r => r.bookId === selectedBook.id);
    const avg = bookRatings.length > 0
      ? parseFloat((bookRatings.reduce((sum, r) => sum + r.rating, 0) / bookRatings.length).toFixed(1))
      : 0;

    const updatedBooks = books.map(b => {
      if (b.id === selectedBook.id) {
        return { ...b, averageRating: avg, ratingCount: bookRatings.length };
      }
      return b;
    });

    setBooks(updatedBooks);
    setSelectedBook(prev => ({ ...prev, averageRating: avg, ratingCount: bookRatings.length }));
    addToast("Review deleted");
  };

  // Book Pruning (Admin Only)
  const handleDeleteBook = (bookId, e) => {
    if (e) e.stopPropagation();
    if (activeUser.role !== 'ADMIN') {
      addToast("Admin credentials required", "error");
      return;
    }

    if (confirm("Are you sure you want to delete this book?")) {
      setBooks(prev => prev.filter(b => b.id !== bookId));
      setRatings(prev => prev.filter(r => r.bookId !== bookId));
      if (selectedBook && selectedBook.id === bookId) {
        setSelectedBook(null);
      }
      addToast("Book deleted successfully!");
    }
  };

  // Save changes to books (Admin Only)
  const handleSaveBook = (e) => {
    e.preventDefault();
    if (activeUser.role !== 'ADMIN') {
      addToast("Admin credentials required", "error");
      return;
    }

    if (!newBookForm.title.trim() || !newBookForm.author.trim() || !newBookForm.isbn.trim()) {
      addToast("Title, Author, and ISBN are required", "error");
      return;
    }

    if (editingBook) {
      // Edit mode
      const updatedBooks = books.map(b => {
        if (b.id === editingBook.id) {
          return {
            ...b,
            ...newBookForm,
            price: parseFloat(newBookForm.price) || 0.0,
            pagecount: parseInt(newBookForm.pagecount) || 0
          };
        }
        return b;
      });
      setBooks(updatedBooks);
      if (selectedBook && selectedBook.id === editingBook.id) {
        setSelectedBook({
          ...selectedBook,
          ...newBookForm,
          price: parseFloat(newBookForm.price) || 0.0,
          pagecount: parseInt(newBookForm.pagecount) || 0
        });
      }
      addToast("Book updated successfully!");
    } else {
      // Create mode
      const newBookObj = {
        ...newBookForm,
        id: Date.now(),
        price: parseFloat(newBookForm.price) || 0.0,
        pagecount: parseInt(newBookForm.pagecount) || 0,
        averageRating: 0.0,
        ratingCount: 0
      };
      setBooks([newBookObj, ...books]);
      addToast(`Book "${newBookObj.title}" created successfully!`);
    }

    setShowModal(false);
    setEditingBook(null);
    setNewBookForm(emptyBookForm);
  };

  // Open Edit Modals
  const openEditModal = (book, e) => {
    if (e) e.stopPropagation();
    setEditingBook(book);
    setNewBookForm({
      title: book.title,
      author: book.author,
      isbn: book.isbn,
      genre: book.genre,
      price: book.price.toString(),
      pagecount: book.pagecount.toString(),
      publisher: book.publisher || "",
      publishingDate: book.publishingDate || "",
      language: book.language || "English",
      description: book.description || ""
    });
    setShowModal(true);
  };

  // Open Create Modal
  const openCreateModal = () => {
    setEditingBook(null);
    setNewBookForm(emptyBookForm);
    setShowModal(true);
  };

  // Search OpenLibrary
  const handleOpenLibrarySearch = async (e) => {
    e.preventDefault();
    if (!olQuery.trim()) return;

    setOlLoading(true);
    setOlError(null);
    setOlResults([]);

    try {
      const response = await fetch(`https://openlibrary.org/search.json?q=${encodeURIComponent(olQuery)}&limit=9`);
      if (!response.ok) throw new Error("API responded with an error status.");
      
      const data = await response.json();
      const docs = data.docs || [];
      
      const results = docs.map((doc, idx) => {
        const title = doc.title || "Untitled";
        const author = doc.author_name ? doc.author_name.join(', ') : "Unknown Author";
        const isbn = doc.isbn ? doc.isbn[0] : `OL-${Math.floor(Math.random() * 100000)}`;
        const pagecount = doc.number_of_pages_median || doc.number_of_pages || Math.floor(Math.random() * 250) + 150;
        const publisher = doc.publisher ? doc.publisher[0] : "OpenLibrary Publisher";
        const firstPublishYear = doc.first_publish_year ? String(doc.first_publish_year) : "2020";
        const language = doc.language ? doc.language[0] : "eng";
        const description = doc.first_sentence ? doc.first_sentence[0] : `A work from OpenLibrary written by ${author}.`;

        return {
          title,
          author,
          isbn,
          pagecount,
          publisher,
          publishingDate: firstPublishYear + "-01-01",
          language: language === 'eng' ? 'English' : language.toUpperCase(),
          description,
          price: 11.99 + (idx % 5)
        };
      });

      setOlResults(results);
      if (results.length === 0) {
        setOlError("No matches found on OpenLibrary.");
      }
    } catch (err) {
      console.error(err);
      setOlError("Failed to communicate with OpenLibrary API.");
    } finally {
      setOlLoading(false);
    }
  };

  const handleImportBook = (olBook) => {
    if (books.some(b => b.isbn === olBook.isbn && olBook.isbn)) {
      addToast("This book is already in your Bookly catalog!", "info");
      return;
    }

    const newBook = {
      ...olBook,
      id: Date.now(),
      averageRating: 0.0,
      ratingCount: 0
    };

    setBooks([newBook, ...books]);
    addToast(`Imported "${olBook.title}" into Bookly!`);
  };

  const getUsernameById = (userId) => {
    const user = registeredUsers.find(u => u.id === userId);
    return user ? user.username : "Anonymous";
  };

  const getUserRoleColor = (userId) => {
    const user = registeredUsers.find(u => u.id === userId);
    if (!user) return 'var(--text-muted)';
    if (user.role === 'ADMIN') return 'var(--accent-secondary)';
    return 'var(--accent-primary)';
  };

  // Filter genres in the dialog box
  const filteredDialogGenres = ALL_GENRES.filter(genre => 
    genre.toLowerCase().includes(dialogSearch.toLowerCase())
  );

  if (!activeUser) {
    return (
      <div className="app-container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', minHeight: '100vh', padding: '1rem' }}>
        {/* Toast Notifications */}
        <div className="toast-container">
          {toasts.map(toast => (
            <div key={toast.id} className={`toast ${toast.type === 'error' ? 'error' : toast.type === 'info' ? 'info' : 'success'}`}>
              <span>{toast.message}</span>
            </div>
          ))}
        </div>

        <div className="auth-card">
          <div className="auth-header">
            <div className="brand-logo" style={{ width: '40px', height: '40px', fontSize: '1.25rem', marginBottom: '0.5rem', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 0.5rem' }}>
              <BookOpen />
            </div>
            <span className="brand-name" style={{ fontSize: '1.75rem', fontWeight: 800, margin: '0', display: 'block', textAlign: 'center' }}>Bookly</span>
            <p className="auth-subtitle" style={{ fontSize: '0.8rem', color: 'var(--text-muted)', textAlign: 'center', marginTop: '0.25rem' }}>Manage & rate your reading collections</p>
          </div>

          <div className="auth-tabs">
            <button 
              className={`auth-tab ${authMode === 'login' ? 'active' : ''}`}
              onClick={() => setAuthMode('login')}
            >
              Login
            </button>
            <button 
              className={`auth-tab ${authMode === 'register' ? 'active' : ''}`}
              onClick={() => setAuthMode('register')}
            >
              Register
            </button>
          </div>

          <div className="auth-body">
            {authMode === 'login' ? (
              <form onSubmit={handleLogin} className="auth-form" style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                  <label style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)' }}>Username or Email</label>
                  <input 
                    type="text" 
                    className="range-number-input"
                    style={{ height: '38px', padding: '0 0.75rem' }}
                    placeholder="e.g. sarah_read or john@example.com"
                    value={usernameInput}
                    onChange={e => setUsernameInput(e.target.value)}
                    required
                  />
                </div>
                
                <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                  <label style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)' }}>Password</label>
                  <input 
                    type="password" 
                    className="range-number-input"
                    style={{ height: '38px', padding: '0 0.75rem' }}
                    placeholder="••••••••"
                    value={passwordInput}
                    onChange={e => setPasswordInput(e.target.value)}
                    required
                  />
                </div>

                <button type="submit" className="btn" style={{ width: '100%', height: '38px', marginTop: '0.5rem' }}>
                  Sign In
                </button>
              </form>
            ) : (
              <form onSubmit={handleRegister} className="auth-form" style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                  <label style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)' }}>Username</label>
                  <input 
                    type="text" 
                    className="range-number-input"
                    style={{ height: '38px', padding: '0 0.75rem' }}
                    placeholder="e.g. bookworm99"
                    value={usernameInput}
                    onChange={e => setUsernameInput(e.target.value)}
                    required
                  />
                </div>

                <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                  <label style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)' }}>Email Address</label>
                  <input 
                    type="email" 
                    className="range-number-input"
                    style={{ height: '38px', padding: '0 0.75rem' }}
                    placeholder="e.g. user@example.com"
                    value={emailInput}
                    onChange={e => setEmailInput(e.target.value)}
                    required
                  />
                </div>
                
                <div className="form-group" style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem' }}>
                  <label style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--text-secondary)' }}>Password</label>
                  <input 
                    type="password" 
                    className="range-number-input"
                    style={{ height: '38px', padding: '0 0.75rem' }}
                    placeholder="••••••••"
                    value={passwordInput}
                    onChange={e => setPasswordInput(e.target.value)}
                    required
                  />
                </div>

                <button type="submit" className="btn" style={{ width: '100%', height: '38px', marginTop: '0.5rem' }}>
                  Create Account
                </button>
              </form>
            )}

            <div className="auth-divider" style={{ display: 'flex', alignItems: 'center', margin: '1rem 0', color: 'var(--text-muted)', fontSize: '0.75rem' }}>
              <div style={{ flexGrow: 1, height: '2px', backgroundColor: 'var(--border-color)' }}></div>
              <span style={{ padding: '0 0.75rem', fontWeight: 700, textTransform: 'uppercase' }}>or</span>
              <div style={{ flexGrow: 1, height: '2px', backgroundColor: 'var(--border-color)' }}></div>
            </div>

            <button type="button" className="btn btn-secondary" style={{ width: '100%', height: '38px' }} onClick={handleGuestLogin}>
              Continue as Guest
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="app-container">
      {/* Toast Notifications */}
      <div className="toast-container">
        {toasts.map(toast => (
          <div key={toast.id} className={`toast ${toast.type === 'error' ? 'error' : toast.type === 'info' ? 'info' : 'success'}`}>
            <span>{toast.message}</span>
          </div>
        ))}
      </div>

      {isFilterDrawerOpen && (
        <div className="mobile-sidebar-overlay" onClick={() => setIsFilterDrawerOpen(false)} />
      )}

      {/* SIDEBAR PANELS */}
      <aside className={`sidebar ${isFilterDrawerOpen ? 'open' : ''}`}>
        <div className="brand">
          <div className="brand-logo">
            <BookOpen />
          </div>
          <span className="brand-name">Bookly</span>
          <button className="mobile-close-sidebar-btn" onClick={() => setIsFilterDrawerOpen(false)}>
            <X size={18} />
          </button>
        </div>

        <nav>
          <ul className="nav-links">
            <li>
              <button 
                onClick={() => { setActiveTab('explore'); setSelectedBook(null); }}
                className={`nav-item ${activeTab === 'explore' ? 'active' : ''}`}
              >
                <Layers />
                <span>Explore Books</span>
              </button>
            </li>
            <li>
              <button 
                onClick={() => { setActiveTab('reviews'); setSelectedBook(null); }}
                className={`nav-item ${activeTab === 'reviews' ? 'active' : ''}`}
              >
                <Star />
                <span>Meine Bewertungen</span>
              </button>
            </li>
            {activeUser.role === 'ADMIN' && (
              <li>
                <button 
                  onClick={() => { setActiveTab('manage'); setSelectedBook(null); }}
                  className={`nav-item ${activeTab === 'manage' ? 'active' : ''}`}
                >
                  <Settings />
                  <span>Settings Books</span>
                </button>
              </li>
            )}
          </ul>
        </nav>

        {/* EXPANDABLE FILTERS SECTION (Only displays on Explorer Tab) */}
        {activeTab === 'explore' && (
          <div className="sidebar-filters-section">
            <div className="filters-title-row">
              <span className="filters-title">Catalog Filters</span>
              {(selectedGenres.length > 0 || selectedAuthors.length > 0 || minPriceFilter > 0 || maxPriceFilter < 100 || selectedLanguages.length > 0 || minRatingFilter > 0 || searchQuery !== '') && (
                <button className="clear-filters-btn" onClick={handleClearFilters}>
                  Clear All
                </button>
              )}
            </div>

            {/* Collapsible Category: Genre */}
            <div className="accordion-item">
              <div className="accordion-header" onClick={() => toggleCollapsible('genre')}>
                <span>Genres ({selectedGenres.length})</span>
                {collapsibles.genre ? <ChevronUp className="accordion-icon" /> : <ChevronDown className="accordion-icon" />}
              </div>
              
              {collapsibles.genre && (
                <div className="accordion-content">
                  {COMMON_GENRES.map(genre => (
                    <label key={genre} className="checkbox-label">
                      <input 
                        type="checkbox" 
                        checked={selectedGenres.includes(genre)}
                        onChange={() => handleGenreCheckbox(genre)}
                      />
                      <span>{genre}</span>
                    </label>
                  ))}
                  <button className="more-filters-btn" onClick={openGenreDialog}>
                    <SlidersHorizontal size={12} />
                    More Genres...
                  </button>
                </div>
              )}
            </div>

            {/* Collapsible Category: Authors */}
            <div className="accordion-item">
              <div className="accordion-header" onClick={() => toggleCollapsible('author')}>
                <span>Authors ({selectedAuthors.length})</span>
                {collapsibles.author ? <ChevronUp className="accordion-icon" /> : <ChevronDown className="accordion-icon" />}
              </div>

              {collapsibles.author && (
                <div className="accordion-content">
                  {COMMON_AUTHORS.map(author => (
                    <label key={author} className="checkbox-label">
                      <input 
                        type="checkbox" 
                        checked={selectedAuthors.includes(author)}
                        onChange={() => handleAuthorCheckbox(author)}
                      />
                      <span>{author}</span>
                    </label>
                  ))}
                  <button className="more-filters-btn" onClick={openAuthorDialog}>
                    <SlidersHorizontal size={12} />
                    More Authors...
                  </button>
                </div>
              )}
            </div>

            {/* Collapsible Category: Price Range */}
            <div className="accordion-item">
              <div className="accordion-header" onClick={() => toggleCollapsible('price')}>
                <span>Price Range</span>
                {collapsibles.price ? <ChevronUp className="accordion-icon" /> : <ChevronDown className="accordion-icon" />}
              </div>

              {collapsibles.price && (
                <div className="accordion-content">
                  <div className="price-slider-group">
                    <div className="dual-range-slider-container">
                      <div className="slider-track" />
                      <div 
                        className="slider-range-fill" 
                        style={{ 
                          left: `${(minPriceFilter / 100) * 100}%`, 
                          right: `${100 - (maxPriceFilter / 100) * 100}%` 
                        }} 
                      />
                      <input 
                        type="range" 
                        min="0" 
                        max="100" 
                        step="1"
                        value={minPriceFilter} 
                        onChange={(e) => {
                          const val = Math.min(Number(e.target.value), maxPriceFilter - 1);
                          setMinPriceFilter(val);
                        }} 
                        className="range-input range-min"
                      />
                      <input 
                        type="range" 
                        min="0" 
                        max="100" 
                        step="1"
                        value={maxPriceFilter} 
                        onChange={(e) => {
                          const val = Math.max(Number(e.target.value), minPriceFilter + 1);
                          setMaxPriceFilter(val);
                        }} 
                        className="range-input range-max"
                      />
                    </div>
                    
                    <div className="range-inputs-container">
                      <div className="range-input-wrapper">
                        <span className="range-input-currency">$</span>
                        <input 
                          type="number" 
                          min="0" 
                          max="100"
                          value={minPriceFilter} 
                          onChange={(e) => {
                            const val = Math.max(0, Math.min(Number(e.target.value), maxPriceFilter));
                            setMinPriceFilter(isNaN(val) ? 0 : val);
                          }}
                          className="range-number-input"
                        />
                      </div>
                      <span className="range-divider">to</span>
                      <div className="range-input-wrapper">
                        <span className="range-input-currency">$</span>
                        <input 
                          type="number" 
                          min="0" 
                          max="100"
                          value={maxPriceFilter} 
                          onChange={(e) => {
                            const val = Math.max(minPriceFilter, Math.min(Number(e.target.value), 100));
                            setMaxPriceFilter(isNaN(val) ? 100 : val);
                          }}
                          className="range-number-input"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* Collapsible Category: Languages */}
            <div className="accordion-item">
              <div className="accordion-header" onClick={() => toggleCollapsible('language')}>
                <span>Languages</span>
                {collapsibles.language ? <ChevronUp className="accordion-icon" /> : <ChevronDown className="accordion-icon" />}
              </div>

              {collapsibles.language && (
                <div className="accordion-content">
                  {["English", "German", "Spanish"].map(lang => (
                    <label key={lang} className="checkbox-label">
                      <input 
                        type="checkbox"
                        checked={selectedLanguages.includes(lang)}
                        onChange={() => handleLanguageCheckbox(lang)}
                      />
                      <span>{lang}</span>
                    </label>
                  ))}
                </div>
              )}
            </div>

            {/* Collapsible Category: Rating */}
            <div className="accordion-item">
              <div className="accordion-header" onClick={() => toggleCollapsible('rating')}>
                <span>Minimum Rating</span>
                {collapsibles.rating ? <ChevronUp className="accordion-icon" /> : <ChevronDown className="accordion-icon" />}
              </div>

              {collapsibles.rating && (
                <div className="accordion-content">
                  <div className="rating-slider-group">
                    <div className="rating-display-row">
                      <span className="rating-value-badge">
                        {minRatingFilter === 0 ? "All Ratings" : `${minRatingFilter.toFixed(1)} ⭐`}
                      </span>
                      {minRatingFilter > 0 && <span className="rating-or-higher">or higher</span>}
                    </div>
                    <input 
                      type="range" 
                      min="0" 
                      max="5" 
                      step="0.1" 
                      value={minRatingFilter} 
                      onChange={(e) => setMinRatingFilter(Number(e.target.value))}
                      className="rating-range-input"
                    />
                    <div className="rating-labels">
                      <span>0.0</span>
                      <span>1.0</span>
                      <span>2.0</span>
                      <span>3.0</span>
                      <span>4.0</span>
                      <span>5.0</span>
                    </div>
                  </div>
                </div>
              )}
            </div>

          </div>
        )}
      </aside>

      {/* MAIN CONTENT WORKSPACE */}
      <main className="main-content">
        
        {/* BIG HEADER BAR */}
        <header className="header-bar">
          <div className="header-brand-title">
            {selectedBook ? "Book Details" : activeTab === 'explore' ? "Bookly" : activeTab === 'manage' ? "Repository Settings" : "Meine Bewertungen"}
          </div>

          {/* BIG CENTRAL SEARCH BAR */}
          {!selectedBook ? (
            <div className="header-search-container">
              <Search />
              <input 
                type="text" 
                className="header-search-bar"
                placeholder="Search catalog titles, author, or ISBN references..."
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
              />
              {activeTab === 'explore' && (
                <button 
                  className="mobile-filter-toggle-btn"
                  onClick={() => setIsFilterDrawerOpen(true)}
                  title="Open Filters"
                >
                  <SlidersHorizontal size={16} />
                </button>
              )}
            </div>
          ) : (
            <div style={{ flexGrow: 1 }}></div>
          )}

          {/* RIGHT SIDE PROFILE TRIGGER & COMMON DROPDOWN */}
          <div className="header-actions-group">
            <div className="header-profile-container" ref={dropdownRef}>
              <div 
                className={`profile-trigger ${showProfileDropdown ? 'active' : ''}`}
                onClick={() => setShowProfileDropdown(!showProfileDropdown)}
              >
                <div className="profile-trigger-avatar">{activeUser.avatar}</div>
                <span className="profile-trigger-name">{activeUser.username}</span>
                <ChevronDown size={14} />
              </div>

              {/* COMMON SETTINGS DROPDOWN */}
              {showProfileDropdown && (
                <div className="profile-dropdown-menu">
                  <div className="profile-dropdown-header">
                    <div className="profile-dropdown-username">{activeUser.username}</div>
                    <div className="profile-dropdown-role">{activeUser.role} Account</div>
                  </div>

                  <button className="profile-dropdown-item" onClick={() => addToast("Redirecting to profile info settings...", "info")}>
                    <User />
                    Account Security
                  </button>

                  <button className="profile-dropdown-item" onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}>
                    {theme === 'dark' ? <Sun /> : <Moon />}
                    Appearance: {theme === 'dark' ? 'Dark' : 'Light'}
                  </button>

                  <div className="profile-dropdown-divider"></div>

                  <button 
                    className="profile-dropdown-item" 
                    onClick={() => { 
                      setActiveUser(null); 
                      setShowProfileDropdown(false); 
                      addToast("Signed out successfully", "info"); 
                    }} 
                    style={{ color: 'var(--danger-color)' }}
                  >
                    <LogOut />
                    Sign Out
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* SCROLLABLE INNER WORKSPACE */}
        <div className="main-content-scrollable">
          {selectedBook ? (
            <div className="book-detail-main-container">
              <div style={{ marginBottom: '1.5rem' }}>
                <button className="back-to-explore-btn" onClick={() => setSelectedBook(null)}>
                  <ArrowLeft size={16} />
                  <span>Back to Catalog</span>
                </button>
              </div>

              <div className="book-detail-main-layout">
                {/* LEFT COLUMN: Cover preview & metadata */}
                <div className="book-detail-left-col">
                  <div className="book-detail-cover">
                    <span className="book-detail-badge-genre">{selectedBook.genre}</span>
                    <span className="book-detail-price-tag">${selectedBook.price.toFixed(2)}</span>
                    <span className="book-detail-cover-art">{selectedBook.title.charAt(0)}</span>
                  </div>

                  <div className="detail-meta-grid" style={{ width: '100%' }}>
                    <div className="meta-item">
                      <span className="meta-label">ISBN Reference</span>
                      <span className="meta-value">{selectedBook.isbn}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-label">Store Price</span>
                      <span className="meta-value" style={{ color: 'var(--success-color)' }}>${selectedBook.price.toFixed(2)}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-label">Publisher</span>
                      <span className="meta-value">{selectedBook.publisher || "N/A"}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-label">Publish Date</span>
                      <span className="meta-value">{selectedBook.publishingDate || "N/A"}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-label">Language</span>
                      <span className="meta-value">{selectedBook.language || "English"}</span>
                    </div>
                    <div className="meta-item">
                      <span className="meta-label">Length</span>
                      <span className="meta-value">{selectedBook.pagecount} Pages</span>
                    </div>
                  </div>
                </div>

                {/* RIGHT COLUMN: Title info & reviews */}
                <div className="book-detail-right-col">
                  <div className="detail-title-section">
                    <h1 className="detail-title" style={{ fontSize: '2.5rem' }}>{selectedBook.title}</h1>
                    <span className="detail-author" style={{ fontSize: '1.35rem' }}>by {selectedBook.author}</span>
                  </div>

                  <div className="detail-title-section">
                    <span className="meta-label">Synopsis Description</span>
                    <p className="detail-desc" style={{ fontSize: '1.05rem', lineHeight: '1.7' }}>
                      {selectedBook.description || "No description provided."}
                    </p>
                  </div>

                  {/* REVIEWS & RATINGS LIST */}
                  <div className="reviews-section">
                    <div className="reviews-header">
                      <span className="reviews-title">User Reviews ({selectedBook.ratingCount || 0})</span>
                      <div className="rating-inline" style={{ fontSize: '1.1rem' }}>
                        <Star style={{ fill: 'var(--rating-color)', color: 'var(--rating-color)' }} />
                        <span>{selectedBook.averageRating > 0 ? selectedBook.averageRating.toFixed(1) : "Unrated"}</span>
                      </div>
                    </div>

                    {/* Submit review */}
                    {activeUser.role !== 'GUEST' ? (
                      <form onSubmit={handleAddRating} className="add-review-form">
                        <span className="form-title">Write a Review</span>
                        
                        <div className="star-rating-select">
                          <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginRight: '0.5rem' }}>Your Rating:</span>
                          {[1, 2, 3, 4, 5].map(starNum => (
                            <button
                              key={starNum}
                              type="button"
                              className={`star-btn ${newRating.rating >= starNum ? 'filled' : ''}`}
                              onClick={() => setNewRating({ ...newRating, rating: starNum })}
                            >
                              <Star />
                            </button>
                          ))}
                        </div>

                        <textarea
                          className="comment-textarea"
                          placeholder="Share your thoughts on this book..."
                          value={newRating.comment}
                          onChange={e => setNewRating({ ...newRating, comment: e.target.value })}
                        />

                        <button type="submit" className="btn" style={{ fontSize: '0.85rem', padding: '0.5rem 1rem', alignSelf: 'flex-end' }}>
                          Submit Review
                        </button>
                      </form>
                    ) : (
                      <div className="add-review-form" style={{ borderStyle: 'solid', textAlign: 'center', padding: '1rem' }}>
                        <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                          Guest profile. Switch user role to review this book.
                        </span>
                      </div>
                    )}

                    {/* Reviews List */}
                    <div className="review-list">
                      {ratings
                        .filter(r => r.bookId === selectedBook.id)
                        .map(review => (
                          <div key={review.id} className="review-card">
                            <div className="review-card-header">
                              <div className="review-user-info">
                                <div className="review-user-avatar">
                                  {getUsernameById(review.userId).charAt(0).toUpperCase()}
                                </div>
                                <div>
                                  <span className="review-username" style={{ color: getUserRoleColor(review.userId) }}>
                                    {getUsernameById(review.userId)}
                                  </span>
                                  <div className="rating-inline" style={{ gap: '2px', marginTop: '2px' }}>
                                    {[1, 2, 3, 4, 5].map(num => (
                                      <Star 
                                        key={num} 
                                        size={12} 
                                        style={{ 
                                          fill: num <= review.rating ? 'var(--rating-color)' : 'none', 
                                          color: num <= review.rating ? 'var(--rating-color)' : 'var(--text-muted)' 
                                        }} 
                                      />
                                    ))}
                                  </div>
                                </div>
                              </div>
                              <span className="review-date">
                                {new Date(review.creationTime).toLocaleDateString()}
                              </span>
                            </div>

                            <p className="review-comment">"{review.comment}"</p>

                            {/* Delete Review (Author or Admin) */}
                            {(activeUser.role === 'ADMIN' || review.userId === activeUser.id) && (
                              <button 
                                className="review-delete-btn"
                                title="Delete Review"
                                onClick={() => handleDeleteRating(review.id)}
                              >
                                <Trash size={14} />
                              </button>
                            )}
                          </div>
                        ))}

                      {ratings.filter(r => r.bookId === selectedBook.id).length === 0 && (
                        <div style={{ textAlign: 'center', padding: '2rem 1rem', color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                          No reviews written yet. Be the first to share your thoughts!
                        </div>
                      )}
                    </div>

                  </div>
                </div>
              </div>
            </div>
          ) : (
            <>
              {/* TAB 1: EXPLORE HUB */}
              {activeTab === 'explore' && (
                <>
                  {/* Dynamic Filter Tag Chips row */}
                  {(selectedGenres.length > 0 || selectedAuthors.length > 0 || minPriceFilter > 0 || maxPriceFilter < 100 || selectedLanguages.length > 0 || minRatingFilter > 0) && (
                    <div className="active-filters-chips-bar">
                      <span className="active-filters-label">Active Filters:</span>
                      
                      {selectedGenres.map(g => (
                        <div key={g} className="filter-chip">
                          <span>Genre: {g}</span>
                          <X size={12} onClick={() => handleGenreCheckbox(g)} />
                        </div>
                      ))}

                      {selectedAuthors.map(a => (
                        <div key={a} className="filter-chip">
                          <span>Author: {a}</span>
                          <X size={12} onClick={() => handleAuthorCheckbox(a)} />
                        </div>
                      ))}

                      {(minPriceFilter > 0 || maxPriceFilter < 100) && (
                        <div className="filter-chip">
                          <span>Price: ${minPriceFilter} - ${maxPriceFilter}</span>
                          <X size={12} onClick={() => {
                            setMinPriceFilter(0);
                            setMaxPriceFilter(100);
                          }} />
                        </div>
                      )}

                      {selectedLanguages.map(l => (
                        <div key={l} className="filter-chip">
                          <span>Language: {l}</span>
                          <X size={12} onClick={() => handleLanguageCheckbox(l)} />
                        </div>
                      ))}

                      {minRatingFilter > 0 && (
                        <div className="filter-chip">
                          <span>Rating: &gt;={minRatingFilter.toFixed(1)} ⭐</span>
                          <X size={12} onClick={() => setMinRatingFilter(0)} />
                        </div>
                      )}
                    </div>
                  )}

              {/* Catalog Toolbar with Sorting */}
              <div className="catalog-toolbar" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
                <span style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', fontWeight: 600 }}>
                  {filteredBooks.length} {filteredBooks.length === 1 ? "book" : "books"} found
                </span>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                  <label htmlFor="sort-select" style={{ fontSize: '0.85rem', color: 'var(--text-muted)', fontWeight: 600 }}>Sort by:</label>
                  <select 
                    id="sort-select" 
                    className="select-filter" 
                    style={{ padding: '0.5rem 2rem 0.5rem 1rem', fontSize: '0.85rem' }}
                    value={sortBy}
                    onChange={e => setSortBy(e.target.value)}
                  >
                    <option value="rating">Top Rated ⭐</option>
                    <option value="reviews">Most Reviews 💬</option>
                    <option value="title">Title (A-Z)</option>
                    <option value="price-low">Price: Low to High</option>
                    <option value="price-high">Price: High to Low</option>
                  </select>
                </div>
              </div>

              {/* Book Cards Grid */}
              {sortedBooks.length > 0 ? (
                <div className="book-grid">
                  {sortedBooks.map(book => (
                    <div 
                      key={book.id} 
                      className="book-card"
                      onClick={() => setSelectedBook(book)}
                    >
                      <div className="book-cover-mock">
                        <span className="book-badge-genre">{book.genre}</span>
                        <span className="book-price-tag">${book.price.toFixed(2)}</span>
                        <span className="book-cover-art">{book.title.charAt(0)}</span>
                      </div>

                      <div className="book-card-body">
                        <h3 className="book-card-title">{book.title}</h3>
                        <span className="book-card-author">by {book.author}</span>
                        
                        <div className="book-card-footer">
                          <div className="rating-inline">
                            <Star />
                            <span>{book.averageRating > 0 ? book.averageRating.toFixed(1) : "N/A"}</span>
                            <span className="rating-count-text">({book.ratingCount || 0})</span>
                          </div>
                          <span className="book-pages-badge">
                            <Clock size={12} />
                            {book.pagecount} p.
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="empty-state">
                  <div className="empty-icon">
                    <Search />
                  </div>
                  <h3 className="empty-title">No books match the criteria</h3>
                  <p className="empty-desc">We couldn't find any books matching your combined filters. Try clearing some constraints.</p>
                  <button className="btn btn-secondary" onClick={handleClearFilters}>Reset Filters</button>
                </div>
              )}
            </>
          )}

          {/* TAB 2: SETTINGS BOOKS (Manage Database) */}
          {activeTab === 'manage' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
              <div className="admin-actions-header">
                <div>
                  <h2 style={{ fontSize: '1.5rem', fontWeight: 850, marginBottom: '0.25rem' }}>Catalog Repository Control</h2>
                  <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                    Inbound REST Controllers map use cases directly to JPA adapters. Secure writes are guarded by roles.
                  </p>
                </div>

                {activeUser.role === 'ADMIN' ? (
                  <button className="btn" onClick={openCreateModal}>
                    <Plus size={18} />
                    Add New Book
                  </button>
                ) : (
                  <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                    (Switch role to <strong>ADMIN</strong> in the profile settings menu to add or edit catalog records)
                  </span>
                )}
              </div>

              <div className="table-container">
                <table className="data-table">
                  <thead>
                    <tr>
                      <th>Title</th>
                      <th>Author</th>
                      <th>ISBN</th>
                      <th>Genre</th>
                      <th>Language</th>
                      <th>Pages</th>
                      <th>Price</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {books.map(book => (
                      <tr key={book.id}>
                        <td style={{ fontWeight: '600' }}>{book.title}</td>
                        <td>{book.author}</td>
                        <td style={{ fontFamily: 'var(--font-heading)', fontSize: '0.85rem' }}>{book.isbn}</td>
                        <td>
                          <span className="ol-badge">{book.genre}</span>
                        </td>
                        <td>{book.language}</td>
                        <td>{book.pagecount}</td>
                        <td className="price-cell">${book.price.toFixed(2)}</td>
                        <td className="actions-cell">
                          <button 
                            className="action-icon-btn" 
                            title="Edit Details"
                            disabled={activeUser.role !== 'ADMIN'}
                            onClick={(e) => openEditModal(book, e)}
                          >
                            <Edit />
                          </button>
                          <button 
                            className="action-icon-btn delete" 
                            title="Delete Book"
                            disabled={activeUser.role !== 'ADMIN'}
                            onClick={(e) => handleDeleteBook(book.id, e)}
                          >
                            <Trash />
                          </button>
                        </td>
                      </tr>
                    ))}
                    {books.length === 0 && (
                      <tr>
                        <td colSpan="8" style={{ textAlign: 'center', padding: '3rem', color: 'var(--text-muted)' }}>
                          No records in the database catalog. Create a book record to begin.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* TAB 4: MY REVIEWS */}
          {activeTab === 'reviews' && (
            <div className="my-reviews-layout">
              <div className="admin-actions-header" style={{ marginBottom: '1.5rem' }}>
                <div>
                  <h2 style={{ fontSize: '1.5rem', fontWeight: 850, marginBottom: '0.25rem' }}>Meine Bewertungen</h2>
                  <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                    Hier siehst du deine verfassten Rezensionen und persönliche Statistiken.
                  </p>
                </div>
              </div>

              {/* Personal Stats Section */}
              <div className="reviews-stats-row" style={{ display: 'flex', gap: '1.5rem', marginBottom: '2rem', flexWrap: 'wrap' }}>
                <div className="stat-card" style={{ flex: '1 1 250px' }}>
                  <div className="stat-icon" style={{ backgroundColor: 'rgba(99, 102, 241, 0.1)', color: 'var(--accent-primary)' }}>
                    <FileText size={20} />
                  </div>
                  <div className="stat-details">
                    <span className="stat-value">{ratings.filter(r => r.userId === activeUser.id).length}</span>
                    <span className="stat-label">Geschriebene Bewertungen</span>
                  </div>
                </div>

                <div className="stat-card" style={{ flex: '1 1 250px' }}>
                  <div className="stat-icon rating" style={{ backgroundColor: 'rgba(245, 158, 11, 0.1)', color: 'var(--rating-color)' }}>
                    <Star style={{ fill: 'var(--rating-color)', color: 'var(--rating-color)' }} size={20} />
                  </div>
                  <div className="stat-details">
                    <span className="stat-value">
                      {ratings.filter(r => r.userId === activeUser.id).length > 0
                        ? (ratings.filter(r => r.userId === activeUser.id).reduce((sum, r) => sum + r.rating, 0) / ratings.filter(r => r.userId === activeUser.id).length).toFixed(1)
                        : "0.0"
                      } ⭐
                    </span>
                    <span className="stat-label">Durchschnittliche Bewertung</span>
                  </div>
                </div>

                <div className="stat-card" style={{ flex: '1 1 250px' }}>
                  <div className="stat-icon secondary" style={{ backgroundColor: 'rgba(168, 85, 247, 0.1)', color: 'var(--accent-secondary)' }}>
                    <BookOpen size={20} />
                  </div>
                  <div className="stat-details">
                    <span className="stat-value">
                      {(() => {
                        const userRatings = ratings.filter(r => r.userId === activeUser.id);
                        if (userRatings.length === 0) return "N/A";
                        const genreCounts = {};
                        userRatings.forEach(r => {
                          const book = books.find(b => b.id === r.bookId);
                          if (book) {
                            genreCounts[book.genre] = (genreCounts[book.genre] || 0) + 1;
                          }
                        });
                        let favoriteGenre = "N/A";
                        let maxCount = 0;
                        for (const genre in genreCounts) {
                          if (genreCounts[genre] > maxCount) {
                            maxCount = genreCounts[genre];
                            favoriteGenre = genre;
                          }
                        }
                        return favoriteGenre;
                      })()}
                    </span>
                    <span className="stat-label">Lieblingsgenre</span>
                  </div>
                </div>
              </div>

              {/* Reviews List */}
              <div className="my-reviews-list-container" style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
                {ratings.filter(r => r.userId === activeUser.id).length > 0 ? (
                  ratings.filter(r => r.userId === activeUser.id).map(review => {
                    const book = books.find(b => b.id === review.bookId);
                    if (!book) return null;
                    return (
                      <div key={review.id} className="review-card" style={{ position: 'relative', padding: '1.5rem' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem', flexWrap: 'wrap', gap: '0.5rem' }}>
                          <div>
                            <h3 
                              style={{ fontSize: '1.1rem', fontWeight: 700, color: 'var(--text-primary)', cursor: 'pointer', display: 'inline-block' }}
                              onClick={() => setSelectedBook(book)}
                              className="review-book-link"
                            >
                              {book.title}
                            </h3>
                            <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginLeft: '0.5rem' }}>by {book.author}</span>
                          </div>
                          <span className="review-date" style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                            {new Date(review.creationTime).toLocaleDateString()}
                          </span>
                        </div>

                        <div className="rating-inline" style={{ gap: '2px', marginBottom: '0.75rem' }}>
                          {[1, 2, 3, 4, 5].map(num => (
                            <Star 
                              key={num} 
                              size={14} 
                              style={{ 
                                fill: num <= review.rating ? 'var(--rating-color)' : 'none', 
                                color: num <= review.rating ? 'var(--rating-color)' : 'var(--text-muted)' 
                              }} 
                            />
                          ))}
                        </div>

                        <p className="review-comment" style={{ fontStyle: 'italic', color: 'var(--text-secondary)', fontSize: '0.95rem', paddingRight: '2rem' }}>
                          "{review.comment}"
                        </p>

                        <button 
                          className="review-delete-btn"
                          title="Löschen"
                          onClick={() => handleDeleteRating(review.id)}
                          style={{ position: 'absolute', right: '1.5rem', bottom: '1.5rem' }}
                        >
                          <Trash size={16} />
                        </button>
                      </div>
                    );
                  })
                ) : (
                  <div className="empty-state" style={{ padding: '3rem' }}>
                    <div className="empty-icon">
                      <Star />
                    </div>
                    <h3 className="empty-title">Keine Bewertungen verfasst</h3>
                    <p className="empty-desc">Du hast für dieses Profil noch keine Bewertungen geschrieben.</p>
                  </div>
                )}
              </div>
            </div>
          )}
            </>
          )}
        </div>
      </main>

      {/* CREATE / EDIT BOOK MODAL */}
      {showModal && (
        <div className="modal-overlay" onClick={() => { setShowModal(false); setEditingBook(null); }}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            
            <div className="modal-header">
              <h2 className="modal-title">{editingBook ? "Modify Book Record" : "Add Book to Repository"}</h2>
              <button className="close-btn" onClick={() => { setShowModal(false); setEditingBook(null); }}>
                <X size={18} />
              </button>
            </div>

            <form onSubmit={handleSaveBook}>
              <div className="modal-body">
                
                <div className="form-grid">
                  <div className="form-group">
                    <label htmlFor="title">Book Title</label>
                    <input 
                      id="title" 
                      type="text" 
                      className="form-input" 
                      placeholder="e.g. Dune"
                      value={newBookForm.title}
                      onChange={e => setNewBookForm({ ...newBookForm, title: e.target.value })}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="author">Author Name</label>
                    <input 
                      id="author" 
                      type="text" 
                      className="form-input" 
                      placeholder="e.g. Frank Herbert"
                      value={newBookForm.author}
                      onChange={e => setNewBookForm({ ...newBookForm, author: e.target.value })}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="isbn">ISBN Code {enriching && <span style={{ fontSize: '0.8rem', color: 'var(--accent-primary)', fontWeight: 650 }}> (✨ Auto-enriching...)</span>}</label>
                    <div style={{ display: 'flex', gap: '0.5rem' }}>
                      <input 
                        id="isbn" 
                        type="text" 
                        className="form-input" 
                        placeholder="e.g. 9780441172719"
                        value={newBookForm.isbn}
                        onChange={e => setNewBookForm({ ...newBookForm, isbn: e.target.value })}
                        onBlur={e => {
                          if (!editingBook && e.target.value.trim().length >= 9) {
                            enrichBookFromOpenLibrary(e.target.value);
                          }
                        }}
                        required
                        style={{ flexGrow: 1 }}
                      />
                      {!editingBook && (
                        <button 
                          type="button" 
                          className="btn btn-secondary" 
                          style={{ padding: '0 1rem', fontSize: '0.85rem', whiteSpace: 'nowrap' }}
                          onClick={() => enrichBookFromOpenLibrary(newBookForm.isbn)}
                          disabled={enriching || !newBookForm.isbn.trim()}
                        >
                          Enrich
                        </button>
                      )}
                    </div>
                  </div>

                  <div className="form-group">
                    <label htmlFor="genre">Genre Category</label>
                    <select 
                      id="genre" 
                      className="select-filter" 
                      style={{ padding: '0.75rem 1rem' }}
                      value={newBookForm.genre}
                      onChange={e => setNewBookForm({ ...newBookForm, genre: e.target.value })}
                    >
                      {ALL_GENRES.map(g => (
                        <option key={g} value={g}>{g}</option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label htmlFor="price">Store Price ($)</label>
                    <input 
                      id="price" 
                      type="number" 
                      step="0.01" 
                      min="0"
                      className="form-input" 
                      placeholder="e.g. 14.99"
                      value={newBookForm.price}
                      onChange={e => setNewBookForm({ ...newBookForm, price: e.target.value })}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="pages">Page Count</label>
                    <input 
                      id="pages" 
                      type="number" 
                      min="1"
                      className="form-input" 
                      placeholder="e.g. 604"
                      value={newBookForm.pagecount}
                      onChange={e => setNewBookForm({ ...newBookForm, pagecount: e.target.value })}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="publisher">Publisher</label>
                    <input 
                      id="publisher" 
                      type="text" 
                      className="form-input" 
                      placeholder="e.g. Chilton Books"
                      value={newBookForm.publisher}
                      onChange={e => setNewBookForm({ ...newBookForm, publisher: e.target.value })}
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="pubdate">Publishing Date</label>
                    <input 
                      id="pubdate" 
                      type="date" 
                      className="form-input" 
                      value={newBookForm.publishingDate}
                      onChange={e => setNewBookForm({ ...newBookForm, publishingDate: e.target.value })}
                    />
                  </div>

                  <div className="form-group full-width">
                    <label htmlFor="description">Synopsis / Description</label>
                    <textarea 
                      id="description" 
                      className="comment-textarea" 
                      placeholder="Enter summary or synopsis details..."
                      style={{ minHeight: '100px' }}
                      value={newBookForm.description}
                      onChange={e => setNewBookForm({ ...newBookForm, description: e.target.value })}
                    />
                  </div>
                </div>

              </div>

              <div className="modal-footer">
                <button 
                  type="button" 
                  className="btn btn-secondary" 
                  onClick={() => { setShowModal(false); setEditingBook(null); }}
                >
                  Cancel
                </button>
                <button type="submit" className="btn">
                  Save Changes
                </button>
              </div>
            </form>

          </div>
        </div>
      )}

      {/* EXTANDABLE DIALOG FOR MULTIPLE FILTER SELECTION (Too many options dialog) */}
      {showGenreDialog && (
        <div className="modal-overlay" onClick={() => setShowGenreDialog(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()} style={{ width: '500px' }}>
            
            <div className="modal-header">
              <h2 className="modal-title">Select Catalog Genres</h2>
              <button className="close-btn" onClick={() => setShowGenreDialog(false)}>
                <X size={18} />
              </button>
            </div>

            <div className="modal-body">
              {/* Search within dialog */}
              <div className="dialog-search-wrapper">
                <Search />
                <input 
                  type="text" 
                  className="dialog-search-input" 
                  placeholder="Filter genres..." 
                  value={dialogSearch}
                  onChange={e => setDialogSearch(e.target.value)}
                />
              </div>

              {/* Grid lists of genres */}
              <div className="dialog-grid-3col">
                {filteredDialogGenres.map(genre => (
                  <label key={genre} className="checkbox-label" style={{ padding: '0.25rem 0' }}>
                    <input 
                      type="checkbox"
                      checked={dialogSelectedGenres.includes(genre)}
                      onChange={() => {
                        setDialogSelectedGenres(prev => 
                          prev.includes(genre) ? prev.filter(g => g !== genre) : [...prev, genre]
                        );
                      }}
                    />
                    <span>{genre}</span>
                  </label>
                ))}
                {filteredDialogGenres.length === 0 && (
                  <div style={{ gridColumn: 'span 3', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.85rem', padding: '1rem' }}>
                    No genres match your query.
                  </div>
                )}
              </div>
            </div>

            <div className="modal-footer">
              <button 
                type="button" 
                className="btn btn-secondary"
                onClick={() => setDialogSelectedGenres([])}
                style={{ marginRight: 'auto', fontSize: '0.85rem', padding: '0.5rem 1rem' }}
              >
                Clear Selection
              </button>
              <button 
                type="button" 
                className="btn btn-secondary" 
                onClick={() => setShowGenreDialog(false)}
              >
                Cancel
              </button>
              <button 
                type="button" 
                className="btn" 
                onClick={applyDialogGenres}
              >
                Apply Filters
              </button>
            </div>

          </div>
        </div>
      )}

      {/* EXTANDABLE DIALOG FOR MULTIPLE AUTHOR SELECTION */}
      {showAuthorDialog && (
        <div className="modal-overlay" onClick={() => setShowAuthorDialog(false)}>
          <div className="modal-content" onClick={e => e.stopPropagation()} style={{ width: '500px' }}>
            
            <div className="modal-header">
              <h2 className="modal-title">Select Catalog Authors</h2>
              <button className="close-btn" onClick={() => setShowAuthorDialog(false)}>
                <X size={18} />
              </button>
            </div>

            <div className="modal-body">
              {/* Search within dialog */}
              <div className="dialog-search-wrapper">
                <Search />
                <input 
                  type="text" 
                  className="dialog-search-input" 
                  placeholder="Filter authors..." 
                  value={dialogSearchAuthor}
                  onChange={e => setDialogSearchAuthor(e.target.value)}
                />
              </div>

              {/* Grid lists of authors */}
              <div className="dialog-grid-3col">
                {Array.from(new Set(books.map(b => b.author))).sort()
                  .filter(author => author.toLowerCase().includes(dialogSearchAuthor.toLowerCase()))
                  .map(author => (
                    <label key={author} className="checkbox-label" style={{ padding: '0.25rem 0' }}>
                      <input 
                        type="checkbox"
                        checked={dialogSelectedAuthors.includes(author)}
                        onChange={() => {
                          setDialogSelectedAuthors(prev => 
                            prev.includes(author) ? prev.filter(a => a !== author) : [...prev, author]
                          );
                        }}
                      />
                      <span>{author}</span>
                    </label>
                  ))}
              </div>
            </div>

            <div className="modal-footer">
              <button 
                type="button" 
                className="btn btn-secondary"
                onClick={() => setDialogSelectedAuthors([])}
                style={{ marginRight: 'auto', fontSize: '0.85rem', padding: '0.5rem 1rem' }}
              >
                Clear Selection
              </button>
              <button 
                type="button" 
                className="btn btn-secondary" 
                onClick={() => setShowAuthorDialog(false)}
              >
                Cancel
              </button>
              <button 
                type="button" 
                className="btn" 
                onClick={applyDialogAuthors}
              >
                Apply Filters
              </button>
            </div>

          </div>
        </div>
      )}

      {/* Mobile Bottom Navigation Bar (AppBar) */}
      <div className="mobile-bottom-nav">
        <button 
          onClick={() => { setActiveTab('explore'); setSelectedBook(null); }}
          className={`mobile-nav-item ${activeTab === 'explore' ? 'active' : ''}`}
        >
          <Layers size={20} />
          <span>Explore</span>
        </button>
        <button 
          onClick={() => { setActiveTab('reviews'); setSelectedBook(null); }}
          className={`mobile-nav-item ${activeTab === 'reviews' ? 'active' : ''}`}
        >
          <Star size={20} />
          <span>Bewertungen</span>
        </button>
        {activeUser.role === 'ADMIN' && (
          <button 
            onClick={() => { setActiveTab('manage'); setSelectedBook(null); }}
            className={`mobile-nav-item ${activeTab === 'manage' ? 'active' : ''}`}
          >
            <Settings size={20} />
            <span>Settings</span>
          </button>
        )}
      </div>

    </div>
  );
}

export default App;
