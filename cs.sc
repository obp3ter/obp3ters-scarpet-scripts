__config() -> {'stay_loaded' -> true};

get_save_dir() -> (return('./cs-saves'));

__command()->(
    _switch();
 );

_spectator()->(
    player=player();

    to_save='echo \''+query(player,'x')+';'+query(player,'y')+';'+query(player,'z')+';'+query(player,'yaw')+';'+query(player,'pitch')+';'+query(player,'dimension')
    +';\' > '+get_save_dir() +'/'+player()~'name'+'.txt';

    global_x=query(player,'x');
    global_y=query(player,'y');
    global_z=query(player,'z');
    global_pitch=query(player,'pitch');
    global_yaw=query(player,'yaw');
    global_dim=query(player,'dimension');

    run('gamemode spectator '+player);

    if(_exec_os_or_powershell('ls '+get_save_dir()) == null,
        _exec_os_or_powershell('mkdir '+get_save_dir());
    );

    _exec_os_or_powershell(to_save);

    return()
);

_survival()->(
    player=player();

    if(global_dim==0,

    ret_values = split(';',_exec_os_or_powershell('cat '+get_save_dir() +'/'+player()~'name'+'.txt'));
    run('gamemode survival '+player);
    run('execute in '+ret_values:5+' run tp '+player+' '+ret_values:0+' '+ret_values:1+' '+ret_values:2+' '+ret_values:3+' '+ret_values:4);
    
    ,//else

    run('gamemode survival '+player);
    run('execute in '+global_dim+' run tp '+player+' '+global_x+' '+global_y+' '+global_z+' '+global_yaw+' '+global_pitch);

    );
    return()
);

_switch()->(
    player=player();
    if(query(player,'gamemode_id')==3,
        (
            _survival()
        ),
        //else if
        query(player,'gamemode_id')==0,
        (
            _spectator()
        )
    )
);

_exec_os_or_powershell(command) -> (
    if(get_os() == 'Linux',
        os_exec(command);,
        powershell_exec(command);
    );
);