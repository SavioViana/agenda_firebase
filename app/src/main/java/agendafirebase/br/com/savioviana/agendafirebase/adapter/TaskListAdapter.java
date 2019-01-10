package agendafirebase.br.com.savioviana.agendafirebase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import agendafirebase.br.com.savioviana.agendafirebase.DAO.AgendaDao;
import agendafirebase.br.com.savioviana.agendafirebase.R;
import agendafirebase.br.com.savioviana.agendafirebase.view.CelulaListView;

public  class TaskListAdapter extends ArrayAdapter {

    public TaskListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View minhaCelulaView;
        minhaCelulaView = convertView;

        CelulaListView celulaListView;


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            minhaCelulaView =inflater.inflate(R.layout.celula_list, parent, false);

            celulaListView = new CelulaListView();

            celulaListView.name = minhaCelulaView.findViewById(R.id.celName);
            celulaListView.phone = minhaCelulaView.findViewById(R.id.celNumber);

            AgendaDao contato = (AgendaDao) this.getItem(position);
            minhaCelulaView.setTag(contato.getReference());

        }else{
            celulaListView = (CelulaListView) minhaCelulaView.getTag();
        }

        AgendaDao agendaDao;

        agendaDao = (AgendaDao) this.getItem(position);

        celulaListView.name.setText(agendaDao.getName());
        celulaListView.phone.setText(agendaDao.getPhone());

        return minhaCelulaView;
    }
}
