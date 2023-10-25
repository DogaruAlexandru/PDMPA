package com.example.client.ui.home;

import static com.example.client.ui.home.HomeViewModel.EMPTY_DAY_LIST;

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

import com.example.client.databinding.FragmentHomeBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //todo add expiration dates in calendar
        bindTextToView();

        final MaterialCalendarView calendarView = binding.calendar;
        bindDecorators(calendarView);
        bindItemsToView(root, calendarView);

        return root;
    }

    private void bindTextToView() {
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
    }

    private void bindDecorators(MaterialCalendarView calendarView) {
        addTodayDecorator(calendarView);
        addDatesDecorator(calendarView);
    }

    private void addDatesDecorator(MaterialCalendarView calendarView) {
        homeViewModel.getDatesWithEvents().observe(getViewLifecycleOwner(), dates -> {
            calendarView.addDecorator(new CalendarDayEventDecorator(dates));
        });
    }

    private void addTodayDecorator(MaterialCalendarView calendarView) {
        CalendarDay today = CalendarDay.today();
        calendarView.setDateSelected(today, true);
        calendarView.addDecorator(new TodayDecorator(today));
    }

    private void bindItemsToView(View root, MaterialCalendarView calendarView) {
        final RecyclerView recyclerView = binding.comingExpirationsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        homeViewModel.getItems().observe(getViewLifecycleOwner(), expirationTodayModels -> {
            recyclerView.setAdapter(new ExpirationsTodayAdaptor(root.getContext(),
                    expirationTodayModels.get(CalendarDay.today())));
            addCalendarDateChangeListener(root, calendarView, recyclerView, expirationTodayModels);
        });
    }

    private static void addCalendarDateChangeListener(View root, MaterialCalendarView calendarView, RecyclerView recyclerView, HashMap<CalendarDay, List<ProducesModel>> expirationTodayModels) {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (!selected) {
                return;
            }
            if (!expirationTodayModels.containsKey(date)) {
                recyclerView.setAdapter(new ExpirationsTodayAdaptor(root.getContext(), EMPTY_DAY_LIST));
                return;
            }
            recyclerView.setAdapter(new ExpirationsTodayAdaptor(root.getContext(), expirationTodayModels.get(date)));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}