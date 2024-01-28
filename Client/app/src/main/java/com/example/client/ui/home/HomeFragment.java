package com.example.client.ui.home;

import static com.example.client.ui.home.HomeViewModel.EMPTY_DAY_LIST;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.data.model.Product;
import com.example.client.databinding.FragmentHomeBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        homeViewModel.setUserId(sharedPreferences.getLong("userId", -1));

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData(root);

        return root;
    }

    private void setData(View root) {
        bindTextToView();

        final MaterialCalendarView calendarView = binding.calendar;
        final CalendarDay today = CalendarDay.today();
        bindDecorators(calendarView, today);
        bindProducesToView(root, calendarView, today);
    }

    private void bindTextToView() {
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }

    private void bindDecorators(MaterialCalendarView calendarView, CalendarDay today) {
        addTodayDecorator(calendarView, today);
        addDatesDecorator(calendarView);
    }

    private void addDatesDecorator(MaterialCalendarView calendarView) {
        homeViewModel.getProducesDates().observe(getViewLifecycleOwner(), dates ->
                calendarView.addDecorator(new CalendarDayEventDecorator(dates)));
    }

    private void addTodayDecorator(MaterialCalendarView calendarView, CalendarDay today) {
        calendarView.setDateSelected(today, true);
        calendarView.addDecorator(new TodayDecorator(today));
    }

    private void bindProducesToView(View root, MaterialCalendarView calendarView, CalendarDay today) {
        final RecyclerView recyclerView = binding.comingExpirationsRecyclerView;

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        homeViewModel.getProduces().observe(getViewLifecycleOwner(), expirationTodayModels -> {
            List<Product> produces = expirationTodayModels.getOrDefault(today, EMPTY_DAY_LIST);
            ExpirationsTodayAdaptor adaptor = new ExpirationsTodayAdaptor(root.getContext(), produces);
            recyclerView.setAdapter(adaptor);
            addCalendarDateChangeListener(root, calendarView, recyclerView, expirationTodayModels);
        });
    }

    private static void addCalendarDateChangeListener(View root, MaterialCalendarView calendarView, RecyclerView recyclerView, Map<CalendarDay, List<Product>> expirationTodayModels) {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (!selected) {
                return;
            }
            List<Product> produces = expirationTodayModels.getOrDefault(date, EMPTY_DAY_LIST);
            recyclerView.setAdapter(new ExpirationsTodayAdaptor(root.getContext(), produces));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        homeViewModel.setData();
        setData(binding.getRoot());
    }
}