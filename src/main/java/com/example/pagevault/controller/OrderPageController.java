package com.example.pagevault.controller;

import com.example.pagevault.model.Book;
import com.example.pagevault.model.Order;
import com.example.pagevault.model.Purchase;
import com.example.pagevault.model.User;
import com.example.pagevault.service.BookService;
import com.example.pagevault.service.OrderService;
import com.example.pagevault.service.PurchaseService;
import com.example.pagevault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class OrderPageController {

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    @GetMapping("/orders/new")
    public String newOrder(Model model) {
        OrderForm form = new OrderForm();
        form.setPaymentMethod("CASH");
        model.addAttribute("orderForm", form);
        model.addAttribute("books", bookService.getAvailableBooks());
        return "order-form";
    }

    @PostMapping("/orders/new")
    public String createOrder(@ModelAttribute("orderForm") OrderForm form,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        List<Book> books = bookService.getAvailableBooks();
        model.addAttribute("books", books);

        if (form.getBookId() == null || form.getQuantity() == null || form.getQuantity() <= 0) {
            model.addAttribute("error", "Please select a book and enter a valid quantity.");
            return "order-form";
        }

        if (form.getPaymentMethod() == null || form.getPaymentMethod().isBlank()) {
            model.addAttribute("error", "Please select a payment method.");
            return "order-form";
        }

        Optional<Book> bookOptional = bookService.getBookById(form.getBookId());
        if (bookOptional.isEmpty()) {
            model.addAttribute("error", "Selected book was not found.");
            return "order-form";
        }

        Book book = bookOptional.get();
        int available = book.getStockQuantity() == null ? 0 : book.getStockQuantity();
        if (available < form.getQuantity()) {
            model.addAttribute("error", "Not enough stock available for this book.");
            return "order-form";
        }

        String username = authentication != null ? authentication.getName() : null;
        User user = username == null ? null : userService.getUserByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Unable to resolve current user.");
            return "order-form";
        }

        Order order = new Order();
        order.setUser(user);
        order.setBook(book);
        order.setQuantity(form.getQuantity());
        order.setStatus("PENDING");
        Order savedOrder = orderService.saveOrder(order);

        book.setStockQuantity(available - form.getQuantity());
        bookService.saveBook(book);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setBook(book);
        purchase.setOrder(savedOrder);
        purchase.setQuantity(form.getQuantity());
        purchase.setTotalAmount((book.getPrice() == null ? 0.0 : book.getPrice()) * form.getQuantity());
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPaymentStatus(Purchase.PaymentStatus.PENDING);
        purchase.setPaymentMethod(form.getPaymentMethod().trim());
        purchaseService.savePurchase(purchase);

        redirectAttributes.addFlashAttribute("success", "Order created successfully.");
        redirectAttributes.addFlashAttribute("orderId", savedOrder.getId());
        redirectAttributes.addFlashAttribute("bookTitle", book.getTitle());
        redirectAttributes.addFlashAttribute("quantity", form.getQuantity());
        redirectAttributes.addFlashAttribute("total", purchase.getTotalAmount());
        redirectAttributes.addFlashAttribute("paymentMethod", purchase.getPaymentMethod());
        return "redirect:/purchases";
    }

    public static class OrderForm {
        private Long bookId;
        private Integer quantity;
        private String paymentMethod;

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }
}

