__config() -> {'stay_loaded' -> true,'scope'->'global','bot'->'GCP Minecraft Bot'};

get_dc_channel() -> (return(dc_channel_from_id('798960815617081345')));

get_world_name_by_folder() -> (return(split('/',system_info('world_path')):(-2))); // or alternatively (return(system_info('world_name')))

__on_discord_message(message) -> (
    if(message~'user'~'is_self',

		if(slice(message~'content',0,1)=='<' && split('>',split('@',message~'content'):1):0 != get_world_name_by_folder(),
			run('tellraw @a {"text":"' + replace(message~'readable_content','\n','\\\\n') + '", "color":"white"}');
  		);
  		return();
    );
    if(message~'channel'!=get_dc_channel()~'name',return());

    text = message~'content';

    if(slice(text,0,1)=='!',
    (
        cmd = split(' ',slice(text,1)):0;

        cmdParams = reduce(split(' ',slice(text,1)),put(_a,split('=',_):0,split('=',_):1);_a,{});

        if(cmd == 'list' && length(cmdParams)==1, 
			e = dc_build_embed();
			dc_build_embed(e,'title','Players Currently Online('+get_world_name_by_folder()+'):');

			for(player('all'),
			age_in_seconds = floor(query(_,'age')/20);
			age_h=floor(age_in_seconds/3600);
			age_m=floor((age_in_seconds%3600)/60);
			age_s=(age_in_seconds%60);
			age_to_print='';
			age_to_print= age_to_print+if(age_h<10,'0'+age_h,age_h);
			age_to_print= age_to_print+if(age_m<10,':0'+age_m,':'+age_m);
			age_to_print= age_to_print+if(age_s<10,':0'+age_s,':'+age_s);

			dc_build_embed(e,'add_field',_,'Has been online for: '+age_to_print);
			);

			dc_send_message(get_dc_channel(),e);
        
            // dc_send_message(message~'channel','Currently online: ' + join(', ',player('all')));
            return();
        );

		if(cmd == 'list' && has(cmdParams,'world') && lower(get(cmdParams,'world'))==lower(get_world_name_by_folder()) ,
                        e_header = dc_build_embed();
                        dc_build_embed(e_header,'title','Players Currently Online('+get_world_name_by_folder()+'):');
                        dc_send_message(get_dc_channel(),e_header);

                        for(player('all'),
                        (
                        e = dc_build_embed();

                        dc_build_embed(e,'title',_);
                        dc_build_embed(e,'thumbnail','https://crafthead.net/helm/'+_);

                        dc_send_message(get_dc_channel(),e);)
                        );

            // dc_send_message(message~'channel','Currently online: ' + join(', ',player('all')));
            return();
        );

   		

        //if(cmd == 'help',  //!help command
        //    dc_send_message(message~'channel','I\'m sorry but i cant help you');
        //    return();
        //);
    ),
    	run('tellraw @a {"text":"<' + dc_get_display_name(message~'user',message~'server') + '@Discord> '
    	 + replace(message~'readable_content','\n','\\\\n') + '", "color":"blue"}');
    );
   
);

__on_chat_message(message,player,command) -> (
	if(!command,
		dc_send_message(get_dc_channel(), '<'+player+'@' + get_world_name_by_folder() + '> '+message);
	);
);

__on_player_connects(player) -> (
	dc_send_message(get_dc_channel(), player+' logged onto the ' + get_world_name_by_folder() + ' server!')
);

__on_player_disconnects(player,reason) -> (
	dc_send_message(get_dc_channel(), player+' logged out from the ' + get_world_name_by_folder() + ' server!')
);

__on_server_starts() -> {
	dc_send_message(get_dc_channel(), get_world_name_by_folder() + ' Server Started!')
};

__on_server_shuts_down() -> {
	dc_send_message(get_dc_channel(), get_world_name_by_folder() + ' Server Shutting Down')
}
