__config() -> {'stay_loaded' -> true};

get_save_dir() -> (return('./cs-saves'));

__command() -> (
    _switch();
 );

 _switch() -> (
    player=player();
    if(query(player,'gamemode_id')==3,
    _back_to_normal()
    ,//else
    _spectator()
    );
);

_spectator() -> (
    player=player();

    global_x=query(player,'x');
    global_y=query(player,'y');
    global_z=query(player,'z');
    global_pitch=query(player,'pitch');
    global_yaw=query(player,'yaw');
    global_dim=query(player,'dimension');
    global_gm=query(player,'gamemode');

    task( _() -> (
    if(_exec_os_or_powershell('ls '+get_save_dir()) == null,
        _exec_os_or_powershell('mkdir '+get_save_dir());
    );

    _exec_os_or_powershell(
        'echo \''+
        global_x+';'+
        global_y+';'+
        global_z+';'+
        global_yaw+';'+
        global_pitch+';'+
        global_dim+';'+
        global_gm+
        ';\' > '+get_save_dir() +'/'+player()~'name'+'.txt');
    ));

    run('gamemode spectator '+player);

    return();
);

_back_to_normal() -> (
    player=player();

    if(global_dim==0,
    ret_val = _exec_os_or_powershell('cat '+get_save_dir() +'/'+player~'name'+'.txt');
    if(ret_val == null,return(););
    ret_values = split(';',ret_val);
    run('gamemode '+ret_values:6+' '+player);
    run('execute in '+ret_values:5+' run tp '+player+' '+ret_values:0+' '+ret_values:1+' '+ret_values:2+' '+ret_values:3+' '+ret_values:4);
    ,//else
    run('gamemode '+global_gm+' '+player);
    run('execute in '+global_dim+' run tp '+player+' '+global_x+' '+global_y+' '+global_z+' '+global_yaw+' '+global_pitch);
    );
    task( _() -> _exec_os_or_powershell('rm '+get_save_dir() +'/'+player~'name'+'.txt'));
    return();
);

_exec_os_or_powershell(command) -> (
    if(get_os() == 'Linux',
        os_exec(command);,
        powershell_exec(command);
    );
);